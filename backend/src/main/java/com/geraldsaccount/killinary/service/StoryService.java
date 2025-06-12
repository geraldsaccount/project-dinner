package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.StoryConfigurationCreationException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryConfigMapper;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.dto.input.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.CreateStoryDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.model.dto.output.shared.StorySummaryDto;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final CharacterRepository characterRepository;
    private final StoryConfigurationRepository configRepository;
    private final StoryConfigMapper configMapper;
    private final CharacterMapper characterMapper;

    public Set<StoryForCreationDto> getStorySummaries() {
        return storyRepository.findAll().stream().map(s -> {
            int minPlayers = Integer.MAX_VALUE;
            int maxPlayers = Integer.MIN_VALUE;
            Set<ConfigDto> configs = new HashSet<>();

            for (StoryConfiguration config : s.getConfigurations()) {
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
    public void createStory(CreateStoryDto input) throws StoryConfigurationCreationException {
        Story story = storyRepository.save(Story.builder()
                .title(input.title())
                .thumbnailDescription(input.thumbnailDescription())
                .shopDescription(input.shopDescription())
                .dinnerStoryBrief(input.dinnerStoryBrief())
                .bannerUrl(input.bannerUrl())
                .build());

        Set<Character> charactersToSave = input.characters().stream()
                .map(dto -> {
                    System.out.println(dto);
                    return characterMapper.asCharacter(dto).withStory(story);
                })
                .collect(Collectors.toSet());

        Set<Character> savedCharacters = characterRepository.saveAll(charactersToSave)
                .stream()
                .collect(Collectors.toSet());

        Map<Integer, Character> characterIndexMap = input.characters().stream()
                .collect(Collectors.toMap(
                        CreateCharacterDto::index,
                        dto -> savedCharacters.stream()
                                .filter(savedChar -> savedChar.getName().equals(dto.name()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(
                                        "Internal error: Saved character not found for DTO name: " + dto.name()))));

        Set<StoryConfiguration> configsToSave = new HashSet<>();
        for (CreateConfigDto configDto : input.configs()) {
            configsToSave.add(buildStoryConfiguration(configDto, story, characterIndexMap));
        }

        Set<StoryConfiguration> savedConfigs = configRepository.saveAll(configsToSave)
                .stream()
                .collect(Collectors.toSet());

        storyRepository.save(story.withCharacters(savedCharacters).withConfigurations(savedConfigs));
    }

    private StoryConfiguration buildStoryConfiguration(
            CreateConfigDto configDto,
            Story story,
            Map<Integer, Character> characterIndexMap) throws StoryConfigurationCreationException {
        Set<Character> configChars = new HashSet<>();

        for (Integer characterIndex : configDto.characterIndices()) {
            Character character = characterIndexMap.get(characterIndex);
            if (character == null) {
                throw new StoryConfigurationCreationException(
                        String.format(
                                "Character with original index %d, referenced in configuration, not found for this story.",
                                characterIndex));
            }
            configChars.add(character);
        }

        return StoryConfiguration.builder()
                .story(story)
                .characters(configChars)
                .build();
    }
}
