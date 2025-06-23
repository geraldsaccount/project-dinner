package com.geraldsaccount.killinary.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.geraldsaccount.killinary.exceptions.MysteryCreationException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryMapper;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterStageInfoDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCrimeDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageEvent;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.CharacterStageInfo;
import com.geraldsaccount.killinary.model.mystery.Crime;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Stage;
import com.geraldsaccount.killinary.model.mystery.StageEvent;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.model.mystery.id.CharacterStageInfoId;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.StageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditorService {
    private static final String CHARACTER_NOT_FOUND_MESSAGE = "Could not find Character after persisting it";
    private final MysteryRepository mysteryRepository;
    private final CharacterRepository characterRepository;
    private final StageRepository stageRepository;
    private final StoryMapper storyMapper;
    private final CharacterMapper characterMapper;

    @Transactional
    public void createMystery(CreateMysteryDto input, Map<String, MultipartFile> files)
            throws MysteryCreationException {
        if (input == null)
            throw new IllegalArgumentException("Input cannot be null");

        Story story = storyMapper.asEntity(input.story());

        MultipartFile bannerFile = files.get("bannerImageFile");
        if (bannerFile != null && !bannerFile.isEmpty()) {
            try {
                story.setBannerImage(bannerFile.getBytes());
            } catch (IOException e) {
                throw new MysteryCreationException("Could not read banner file bytes", e);
            }
        }

        Map<String, Character> characters = saveCharacters(input.characters(), files);

        Map<String, Stage> stages = saveStages(input.stages());

        characters = enrichCharactersWithStageInfo(characters, stages, input.characters());
        characters = enrichCharactersWithRelationships(characters, input.characters());

        List<PlayerConfig> configs = buildPlayerConfigs(characters, input.setups());

        Crime crime = buildCrime(characters, input.crime());

        saveMystery(
                story,
                characters.values().stream().toList(),
                stages.values().stream().toList(),
                configs, crime);
    }

    private Map<String, Character> saveCharacters(List<CreateCharacterDto> input, Map<String, MultipartFile> files)
            throws MysteryCreationException {
        List<Character> charactersToSave = input.stream().map(characterMapper::asEntity).toList();

        Map<String, Character> dtoIdToEntityMap = input.stream()
                .collect(Collectors.toMap(
                        CreateCharacterDto::id,
                        dto -> charactersToSave.get(input.indexOf(dto))));

        // --- MODIFICATION FOR AVATARS ---
        for (CreateCharacterDto dto : input) {
            MultipartFile avatarFile = files.get(dto.id());
            if (avatarFile != null && !avatarFile.isEmpty()) {
                Character character = dtoIdToEntityMap.get(dto.id());
                if (character != null) {
                    try {
                        character.setAvatarImage(avatarFile.getBytes());
                    } catch (IOException e) {
                        throw new MysteryCreationException(
                                "Could not read avatar file bytes for character: " + dto.name(), e);
                    }
                }
            }
        }

        List<Character> persistedCharacters = characterRepository.saveAll(charactersToSave);

        return mapByIdAndMatch(input,
                persistedCharacters,
                CreateCharacterDto::id,
                CreateCharacterDto::name,
                Character::getName,
                () -> new MysteryCreationException(CHARACTER_NOT_FOUND_MESSAGE));
    }

    private Map<String, Stage> saveStages(List<CreateStageDto> input) throws MysteryCreationException {
        // Map from temporary string id (from DTO) to Stage entity
        List<Stage> persistedStages = stageRepository
                .saveAll(input.stream().map(dto -> Stage.builder()
                        .order(dto.order())
                        .title(dto.title())
                        .hostPrompt(dto.hostPrompt())
                        .build()).toList());

        return mapByIdAndMatch(input,
                persistedStages,
                CreateStageDto::id,
                CreateStageDto::title,
                Stage::getTitle,
                () -> new MysteryCreationException("Could not find Stage after persisting it"));
    }

    private Map<String, Character> enrichCharactersWithStageInfo(Map<String, Character> characters,
            Map<String, Stage> stages,
            List<CreateCharacterDto> input) throws MysteryCreationException {
        for (CreateCharacterDto createCharacter : input) {
            if (createCharacter == null)
                throw new MysteryCreationException("CreateCharacterDto is null");
            Character character = characters.get(createCharacter.id());
            if (character == null) {
                throw new MysteryCreationException(
                        "Character with id " + createCharacter.id() + " not found in characters map.");
            }
            if (createCharacter.stageInfo() == null)
                throw new MysteryCreationException("Stage info for character " + createCharacter.id() + " is null");

            Character withStageInfo = character.withStageInfo(createCharacter.stageInfo().stream()
                    .map(csi -> buildStageInfoOrThrow(stages, character, csi))
                    .toList());

            characters.put(createCharacter.id(), withStageInfo);
        }
        List<Character> enrichedCharacters = characterRepository.saveAll(characters.values().stream().toList());
        return mapByIdAndMatch(input,
                enrichedCharacters,
                CreateCharacterDto::id,
                CreateCharacterDto::name,
                Character::getName,
                () -> new MysteryCreationException(CHARACTER_NOT_FOUND_MESSAGE));
    }

    private CharacterStageInfo buildStageInfoOrThrow(Map<String, Stage> stages, Character character,
            CreateCharacterStageInfoDto csi) {
        if (csi.stageId() == null)
            throw new MysteryCreationException("StageInfo stageId is null");
        if (!stages.containsKey(csi.stageId()))
            throw new MysteryCreationException("Stage with id " + csi.stageId() + " not found");
        Stage stage = stages.get(csi.stageId());
        return buildStageInfo(csi, character.getId(), stage);
    }

    private CharacterStageInfo buildStageInfo(CreateCharacterStageInfoDto input, UUID characterId,
            Stage stage) {
        return CharacterStageInfo.builder()
                .id(new CharacterStageInfoId(stage.getId(), characterId))
                .order(stage.getOrder())
                .objectivePrompt(input.objectivePrompt())
                .events(input.events() == null ? List.of()
                        : input.events().stream().map(this::buildStageEvent).toList())
                .build();
    }

    private StageEvent buildStageEvent(CreateStageEvent input) {
        if (input.id() == null)
            throw new MysteryCreationException("StageEvent id is null");
        return StageEvent.builder()
                .order(input.order())
                .time(input.time())
                .title(input.title())
                .description(input.description())
                .build();
    }

    private Map<String, Character> enrichCharactersWithRelationships(Map<String, Character> characters,
            List<CreateCharacterDto> input) throws MysteryCreationException {
        for (CreateCharacterDto createCharacter : input) {
            if (createCharacter == null)
                throw new MysteryCreationException("CreateCharacterDto is null");
            Character current = characters.get(createCharacter.id());
            if (current == null) {
                throw new MysteryCreationException(
                        "Character with id " + createCharacter.id() + " not found in characters map.");
            }
            if (createCharacter.relationships() == null)
                throw new MysteryCreationException("Relationships for character " + createCharacter.id() + " is null");

            // Map<String, String> (DTO) -> Map<UUID, String> (Entity)
            Map<UUID, String> relationships = createCharacter.relationships().entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> characters.get(e.getKey()).getId(),
                            Map.Entry::getValue));

            Character withRelationships = current.withRelationships(relationships);

            characters.put(createCharacter.id(), withRelationships);
        }
        List<Character> enrichedCharacters = characterRepository.saveAll(characters.values().stream().toList());
        return mapByIdAndMatch(input,
                enrichedCharacters,
                CreateCharacterDto::id,
                CreateCharacterDto::name,
                Character::getName,
                () -> new MysteryCreationException(CHARACTER_NOT_FOUND_MESSAGE));
    }

    private List<PlayerConfig> buildPlayerConfigs(Map<String, Character> characters, List<CreateConfigDto> input)
            throws MysteryCreationException {
        if (input == null)
            throw new MysteryCreationException("Config input list cannot be null");
        return input.stream()
                .map(cdto -> {
                    if (cdto == null)
                        throw new MysteryCreationException("CreateConfigDto is null");
                    if (cdto.id() == null)
                        throw new MysteryCreationException("Config id is null");
                    if (cdto.characterIds() == null || cdto.characterIds().isEmpty())
                        throw new MysteryCreationException("Config characterIds is null or empty");
                    var configCharacters = characters.entrySet().stream()
                            .filter(entry -> cdto.characterIds().contains(entry.getKey()))
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toSet());
                    if (configCharacters.isEmpty())
                        throw new MysteryCreationException("No valid characters found for config " + cdto.id());
                    return PlayerConfig.builder()
                            .playerCount(cdto.playerCount())
                            .characters(configCharacters)
                            .build();
                })
                .toList();
    }

    private Crime buildCrime(Map<String, Character> characters, CreateCrimeDto input) throws MysteryCreationException {
        if (input == null)
            throw new MysteryCreationException("Crime input cannot be null");
        Set<Character> criminals = input.criminalIds().stream()
                .map(characters::get)
                .collect(Collectors.toSet());

        return Crime.builder()
                .criminals(criminals)
                .description(input.description())
                .build();
    }

    private Mystery saveMystery(Story story, List<Character> characters, List<Stage> stages, List<PlayerConfig> setups,
            Crime crime) throws MysteryCreationException {
        if (story == null)
            throw new MysteryCreationException("Story is null");
        if (characters == null)
            throw new MysteryCreationException("Characters list is null");
        if (stages == null)
            throw new MysteryCreationException("Stages list is null");
        if (setups == null)
            throw new MysteryCreationException("Setups list is null");
        if (crime == null)
            throw new MysteryCreationException("Crime is null");
        return mysteryRepository.save(Mystery.builder()
                .story(story)
                .characters(characters)
                .stages(stages)
                .setups(setups)
                .crime(crime)
                .build());
    }

    public static <D, E, I> Map<I, E> mapByIdAndMatch(
            List<D> dtos,
            List<E> entities,
            Function<D, I> idExtractor,
            Function<D, String> dtoNameExtractor,
            Function<E, String> entityNameExtractor,
            Supplier<RuntimeException> notFoundSupplier) {
        return dtos.stream()
                .collect(Collectors.toMap(
                        idExtractor,
                        dto -> entities.stream()
                                .filter(e -> entityNameExtractor.apply(e).equals(dtoNameExtractor.apply(dto)))
                                .findFirst()
                                .orElseThrow(notFoundSupplier)));
    }
}
