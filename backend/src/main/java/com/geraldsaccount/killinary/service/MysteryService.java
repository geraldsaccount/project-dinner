package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.StoryConfigurationCreationException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryConfigMapper;
import com.geraldsaccount.killinary.mappers.StoryMapper;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterStageInfoDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCrimeDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageEvent;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStoryDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.model.dto.output.shared.StorySummaryDto;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Stage;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.model.mystery.id.CharacterStageInfoId;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.CharacterStageInfo;
import com.geraldsaccount.killinary.model.mystery.Crime;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.StageEvent;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MysteryService {
    private final MysteryRepository mysteryRepository;
    private final StoryRepository storyRepository;
    private final CharacterRepository characterRepository;
    private final StoryConfigurationRepository configRepository;
    private final StoryConfigMapper configMapper;
    private final StoryMapper storyMapper;
    private final CharacterMapper characterMapper;

    public Set<StoryForCreationDto> getStorySummaries() {
        return storyRepository.findAll().stream().map(s -> {
            int minPlayers = Integer.MAX_VALUE;
            int maxPlayers = Integer.MIN_VALUE;
            Set<ConfigDto> configs = new HashSet<>();

            for (PlayerConfig config : s.getConfigurations()) {
                int playerCount = config.getCharacters().size();
                minPlayers = Math.min(minPlayers, playerCount);
                maxPlayers = Math.max(maxPlayers, playerCount);
                configs.add(configMapper.asSummaryDTO(config));
            }

            Set<CharacterDetailDto> characters = s.getConfigurations().stream()
                    .flatMap(cfg -> cfg.getCharacters().stream())
                    .map(c -> characterMapper.asDetailDTO(c))
                    .collect(Collectors.toSet());

            return new StoryForCreationDto(
                    new StorySummaryDto(s.getId(),
                            s.getTitle(),
                            s.getBannerUrl(),
                            s.getThumbnailDescription()),
                    minPlayers,
                    maxPlayers,
                    characters,
                    configs);
        }).collect(Collectors.toSet());
    }

    public Story getStoryOrThrow(UUID id) throws StoryNotFoundException {
        return storyRepository.findById(id).orElseThrow(() -> new StoryNotFoundException("Could not find Story."));
    }

    @Transactional
    public void createMystery(CreateMysteryDto input) throws StoryConfigurationCreationException {
        Story story = storyMapper.asEntity(input.story());

        Map<UUID, Character> characters = buildCharacters(input.characters());
        Map<UUID, Stage> stages = buildStages(input.stages());

        enrichCharactersWithStageInfo(characters, stages, input.characters());

        List<PlayerConfig> configs = buildPlayerConfigs(characters, input.setups());

        Crime crime = buildCrime(characters, input.crime());

        Mystery mystery = saveMystery(
                story,
                characters.values().stream().toList(),
                stages.values().stream().toList(),
                configs, crime);
    }

    private Map<UUID, Character> buildCharacters(List<CreateCharacterDto> input) {
        return input.stream()
                .map(characterMapper::asEntity)
                .collect(Collectors.toMap(Character::getId, c -> c));
    }

    private Map<UUID, Stage> buildStages(List<CreateStageDto> input) {
        return input.stream().map((s) -> Stage.builder()
                .id(s.id())
                .order(s.order())
                .title(s.title())
                .hostPrompt(s.hostPrompt())
                .build())
                .collect(Collectors.toMap(Stage::getId, c -> c));
    }

    private void enrichCharactersWithStageInfo(Map<UUID, Character> characters, Map<UUID, Stage> stages,
            List<CreateCharacterDto> input) {
        for (CreateCharacterDto createCharacter : input) {
            Character current = characters.get(createCharacter.id());

            Character withStageInfo = current.withStageInfo(createCharacter.stageInfo().stream()
                    .map(csi -> {
                        CharacterStageInfo stageInfo = CharacterStageInfo.builder()
                                .id(new CharacterStageInfoId(csi.stageId(), current.getId()))
                                .order(stages.get(csi.stageId()).getOrder())
                                .objectivePrompt(csi.objectivePrompt())
                                .events(csi.stageEvents().stream().map(ev -> StageEvent.builder()
                                        .id(ev.id())
                                        .order(ev.order())
                                        .time(ev.time())
                                        .title(ev.title())
                                        .description(ev.description())
                                        .build())
                                        .toList())
                                .build();
                        return stageInfo;
                    })
                    .toList());

            characters.put(current.getId(), withStageInfo);
        }
    }

    private List<PlayerConfig> buildPlayerConfigs(Map<UUID, Character> characters, List<CreateConfigDto> input) {
        return input.stream()
                .map(cdto -> PlayerConfig.builder()
                        .id(cdto.id())
                        .playerCount(cdto.playerCount())
                        .characters(characters.values().stream()
                                .filter(c -> cdto.characterIds().contains(c.getId()))
                                .collect(Collectors.toSet()))
                        .build())
                .toList();
    }

    private Crime buildCrime(Map<UUID, Character> characters, CreateCrimeDto input) {
        return Crime.builder()
                .criminals(characters.values().stream()
                        .filter(c -> input.criminalIds().contains(c.getId())).collect(Collectors.toSet()))
                .description(input.description())
                .build();
    }

    private Mystery saveMystery(Story story, List<Character> characters, List<Stage> stages, List<PlayerConfig> setups,
            Crime crime) {
        return mysteryRepository.save(Mystery.builder()
                .story(story)
                .characters(characters)
                .stages(stages)
                .setups(setups)
                .crime(crime)
                .build());
    }
}
