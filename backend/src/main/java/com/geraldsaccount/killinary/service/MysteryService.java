package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.MysteryNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.PlayerConfigMapper;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.model.dto.output.shared.StorySummaryDto;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.repository.MysteryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MysteryService {
    private final MysteryRepository mysteryRepository;
    private final PlayerConfigMapper configMapper;
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
                    .map(characterMapper::asDetailDTO)
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

}
