package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryConfigMapper;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.model.dto.output.shared.StorySummaryDto;
import com.geraldsaccount.killinary.repository.StoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final StoryConfigMapper configMapper;
    private final CharacterMapper characterMapper;

    public Set<StoryForCreationDto> getStorySummaries() {
        return storyRepository.findAll().stream().map(s -> {
            int minPlayers = Integer.MAX_VALUE;
            int maxPlayers = Integer.MIN_VALUE;
            Set<ConfigDto> configs = new HashSet<>();

            for (StoryConfiguration config : s.getConfigurations()) {
                int playerCount = config.getPlayerCount();
                minPlayers = Math.min(minPlayers, playerCount);
                maxPlayers = Math.max(maxPlayers, playerCount);
                configs.add(configMapper.asSummaryDTO(config));
            }

            Set<CharacterDetailDto> characters = s.getConfigurations().stream()
                    .flatMap(cfg -> cfg.getCharactersInConfig().stream())
                    .map(scc -> characterMapper.asDetailDTO(scc.getCharacter()))
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
}
