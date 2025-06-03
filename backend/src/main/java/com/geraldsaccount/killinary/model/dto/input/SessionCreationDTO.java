package com.geraldsaccount.killinary.model.dto.input;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionCreationDTO(UUID storyId, LocalDateTime eventStart, UUID storyConfigurationId) {

}
