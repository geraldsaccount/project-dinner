package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.MysteryCreationException;
import com.geraldsaccount.killinary.exceptions.MysteryNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationCreationException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.PlayerConfigMapper;
import com.geraldsaccount.killinary.mappers.StoryMapper;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCrimeDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.model.dto.output.shared.StorySummaryDto;
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
public class MysteryService {
    private final MysteryRepository mysteryRepository;
    private final CharacterRepository characterRepository;
    private final StageRepository stageRepository;
    private final PlayerConfigMapper configMapper;
    private final StoryMapper storyMapper;
    private final CharacterMapper characterMapper;

    public Set<StoryForCreationDto> getMysterySummaries() {
        return mysteryRepository.findAll().stream().map(m -> {
            int minPlayers = Integer.MAX_VALUE;
            int maxPlayers = Integer.MIN_VALUE;
            Set<ConfigDto> configs = new HashSet<>();

            for (PlayerConfig config : m.getSetups()) {
                int playerCount = config.getCharacters().size();
                minPlayers = Math.min(minPlayers, playerCount);
                maxPlayers = Math.max(maxPlayers, playerCount);
                configs.add(configMapper.asSummaryDTO(config));
            }

            Set<CharacterDetailDto> characters = m.getSetups().stream()
                    .flatMap(cfg -> cfg.getCharacters().stream())
                    .map(c -> characterMapper.asDetailDTO(c))
                    .collect(Collectors.toSet());

            return new StoryForCreationDto(
                    m.getId(),
                    new StorySummaryDto(
                            m.getStory().getTitle(),
                            m.getStory().getBannerUrl(),
                            m.getStory().getShopDescription()),
                    minPlayers,
                    maxPlayers,
                    characters,
                    configs);
        }).collect(Collectors.toSet());
    }

    public Mystery getMysteryOrThrow(UUID id) throws MysteryNotFoundException {
        return mysteryRepository.findById(id).orElseThrow(() -> new MysteryNotFoundException("Could not find Story."));
    }

    @Transactional
    public void createMystery(CreateMysteryDto input)
            throws StoryConfigurationCreationException, MysteryCreationException {
        if (input == null)
            throw new IllegalArgumentException("Input cannot be null");

        Story story = storyMapper.asEntity(input.story());

        Map<String, Character> characters = saveCharacters(input.characters());

        Map<String, Stage> stages = saveStages(input.stages());

        characters = enrichCharactersWithStageInfo(characters, stages, input.characters());
        characters = enrichCharactersWithRelationships(characters, input.characters());

        List<PlayerConfig> configs = buildPlayerConfigs(characters, input.setups());

        Crime crime = buildCrime(characters, input.crime());

        Mystery mystery = saveMystery(
                story,
                characters.values().stream().toList(),
                stages.values().stream().toList(),
                configs, crime);
    }

    private Map<String, Character> saveCharacters(List<CreateCharacterDto> input) throws MysteryCreationException {
        // Map from temporary string id (from DTO) to Character entity
        List<Character> persistedCharacters = characterRepository
                .saveAll(input.stream().map((c) -> characterMapper.asEntity(c)).toList());
        return input.stream()
                .collect(Collectors.toMap(
                        CreateCharacterDto::id, // temporary string id from DTO
                        dto -> persistedCharacters.stream()
                                .filter(c -> c.getName().equals(dto.name()))
                                .findFirst()
                                .orElseThrow(
                                        () -> new MysteryCreationException(
                                                "Could not find Character after persisting it"))));
    }

    private Map<String, Stage> saveStages(List<CreateStageDto> input) throws MysteryCreationException {
        // Map from temporary string id (from DTO) to Stage entity
        List<Stage> persistedStages = stageRepository
                .saveAll(input.stream().map((dto) -> Stage.builder()
                        .order(dto.order())
                        .title(dto.title())
                        .hostPrompt(dto.hostPrompt())
                        .build()).toList());
        return input.stream()
                .collect(Collectors.toMap(
                        CreateStageDto::id, // temporary string id from DTO
                        dto -> persistedStages.stream()
                                .filter(c -> c.getTitle().equals(dto.title()))
                                .findFirst()
                                .orElseThrow(
                                        () -> new MysteryCreationException(
                                                "Could not find Stage after persisting it"))));
    }

    private Map<String, Character> enrichCharactersWithStageInfo(Map<String, Character> characters,
            Map<String, Stage> stages,
            List<CreateCharacterDto> input) throws MysteryCreationException {
        for (CreateCharacterDto createCharacter : input) {
            if (createCharacter == null)
                throw new MysteryCreationException("CreateCharacterDto is null");
            Character current = characters.get(createCharacter.id());
            if (current == null) {
                throw new MysteryCreationException(
                        "Character with id " + createCharacter.id() + " not found in characters map.");
            }
            if (createCharacter.stageInfo() == null)
                throw new MysteryCreationException("Stage info for character " + createCharacter.id() + " is null");

            Character withStageInfo = current.withStageInfo(createCharacter.stageInfo().stream()
                    .map(csi -> {

                        if (csi.stageId() == null)
                            throw new MysteryCreationException("StageInfo stageId is null");
                        if (!stages.containsKey(csi.stageId()))
                            throw new MysteryCreationException("Stage with id " + csi.stageId() + " not found");
                        Stage stage = stages.get(csi.stageId());
                        CharacterStageInfo stageInfo = CharacterStageInfo.builder()
                                .id(new CharacterStageInfoId(stage.getId(), current.getId()))
                                .order(stages.get(csi.stageId()).getOrder())
                                .objectivePrompt(csi.objectivePrompt())
                                .events(csi.stageEvents() == null ? List.of() : csi.stageEvents().stream().map(ev -> {
                                    if (ev.id() == null)
                                        throw new MysteryCreationException("StageEvent id is null");
                                    return StageEvent.builder()
                                            .order(ev.order())
                                            .time(ev.time())
                                            .title(ev.title())
                                            .description(ev.description())
                                            .build();
                                }).toList())
                                .build();
                        return stageInfo;
                    })
                    .toList());

            characters.put(createCharacter.id(), withStageInfo);
        }
        List<Character> enrichedCharacters = characterRepository.saveAll(characters.values().stream().toList());
        return input.stream()
                .collect(Collectors.toMap(
                        CreateCharacterDto::id,
                        dto -> enrichedCharacters.stream()
                                .filter(c -> c.getName().equals(dto.name()))
                                .findFirst()
                                .orElseThrow(
                                        () -> new MysteryCreationException(
                                                "Could not find Character after persisting it"))));
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
        return input.stream()
                .collect(Collectors.toMap(
                        CreateCharacterDto::id,
                        dto -> enrichedCharacters.stream()
                                .filter(c -> c.getName().equals(dto.name()))
                                .findFirst()
                                .orElseThrow(
                                        () -> new MysteryCreationException(
                                                "Could not find Character after persisting it"))));
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
                            .map(entry -> entry.getValue())
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
        var criminals = characters.values().stream()
                .filter(c -> input.criminalIds().contains(c.getId())).collect(Collectors.toSet());
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
}
