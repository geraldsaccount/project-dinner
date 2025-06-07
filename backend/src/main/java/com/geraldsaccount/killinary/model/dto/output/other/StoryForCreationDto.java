package com.geraldsaccount.killinary.model.dto.output.other;

import java.util.Set;

import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.shared.StorySummaryDto;

public record StoryForCreationDto(
        StorySummaryDto story,
        int minPlayerCount,
        int maxPlayerCount,
        Set<CharacterDetailDto> characters,
        Set<ConfigDto> configs) {

}
