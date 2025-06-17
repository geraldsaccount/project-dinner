package com.geraldsaccount.killinary.model.dto.output.other;

import java.util.Set;
import java.util.UUID;

import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.shared.StorySummaryDto;

public record StoryForCreationDto(
        UUID uuid,
        StorySummaryDto story,
        int minPlayerCount,
        int maxPlayerCount,
        Set<CharacterDetailDto> characters,
        Set<ConfigDto> configs) {

}
