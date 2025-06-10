package com.geraldsaccount.killinary.model.dto.output.shared;

import java.util.UUID;

public record CharacterSummaryDto(
        UUID uuid,
        String name,
        String avatarUrl,
        String role) {

}
