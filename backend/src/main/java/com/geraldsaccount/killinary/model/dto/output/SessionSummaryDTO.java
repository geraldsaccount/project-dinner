package com.geraldsaccount.killinary.model.dto.output;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record SessionSummaryDTO(UUID sessionId,
        String hostName,
        String storyName,
        String assignedCharacterName,
        LocalDateTime sessionDate,
        Boolean isHost) {

}
