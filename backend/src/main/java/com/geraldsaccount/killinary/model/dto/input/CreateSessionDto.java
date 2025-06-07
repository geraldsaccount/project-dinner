package com.geraldsaccount.killinary.model.dto.input;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CreateSessionDto(
        UUID storyId,
        LocalDateTime eventStart,
        UUID storyConfigurationId) {
}
