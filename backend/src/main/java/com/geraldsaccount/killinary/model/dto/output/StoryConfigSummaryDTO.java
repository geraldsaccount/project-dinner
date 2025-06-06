package com.geraldsaccount.killinary.model.dto.output;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.geraldsaccount.killinary.model.Gender;

import lombok.Builder;

@Builder
public record StoryConfigSummaryDTO(UUID id,
        Integer playerCount,
        Map<Gender, Integer> genderCounts,
        Set<UUID> characterIds) {

}
