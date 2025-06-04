package com.geraldsaccount.killinary.model.dto.input;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record SessionCreationDTO(UUID storyId, LocalDateTime eventStart, UUID storyConfigurationId) {

}
