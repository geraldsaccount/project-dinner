package com.geraldsaccount.killinary.model.dto.output.other;

import java.util.Set;
import java.util.UUID;

import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;

public record StoryForCreationDto(
        UUID uuid,
        String title,
        String thumbnailDescription,
        String bannerUrl,
        int minPlayerCount,
        int maxPlayerCount,
        Set<CharacterDetailDto> characters,
        Set<ConfigDto> configs) {

}
