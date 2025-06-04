package com.geraldsaccount.killinary.model.dto.output;

import java.util.Set;
import java.util.UUID;

import lombok.Builder;

@Builder
public record StorySummaryDTO(UUID id, String title, String thumbnailDescription, Integer minPlayers,
        Integer maxPlayers, Set<StoryConfigSummaryDTO> configs) {

}
