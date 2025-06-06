package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryConfigMapper;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.dto.output.StoryConfigSummaryDTO;
import com.geraldsaccount.killinary.model.dto.output.StorySummaryDTO;
import com.geraldsaccount.killinary.repository.StoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final StoryConfigMapper configMapper;
    private final CharacterMapper characterMapper;

    public Set<StorySummaryDTO> getStorySummaries() {
        Set<StorySummaryDTO> summaries = new HashSet<>();
        storyRepository.findAll().stream().map(s -> {
            int minPlayers = Integer.MAX_VALUE;
            int maxPlayers = Integer.MIN_VALUE;
            Set<StoryConfigSummaryDTO> configs = new HashSet<>();

            for (StoryConfiguration config : s.getConfigurations()) {
                int playerCount = config.getPlayerCount();
                minPlayers = Math.min(minPlayers, playerCount);
                maxPlayers = Math.max(maxPlayers, playerCount);
                configs.add(configMapper.asSummaryDTO(config, characterMapper::asSummaryDTO));
            }

            return StorySummaryDTO.builder()
                    .id(s.getId())
                    .title(s.getTitle())
                    .thumbnailDescription(s.getShopDescription())
                    .minPlayers(minPlayers)
                    .maxPlayers(maxPlayers)
                    .configs(configs)
                    .build();
        }).forEach(summaries::add);

        return summaries;
    }

    public Story getStoryOrThrow(UUID id) throws StoryNotFoundException {
        return storyRepository.findById(id).orElseThrow(() -> new StoryNotFoundException("Could not find Story."));
    }
}
