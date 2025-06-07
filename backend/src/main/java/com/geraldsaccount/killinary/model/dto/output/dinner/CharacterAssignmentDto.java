package com.geraldsaccount.killinary.model.dto.output.dinner;

import java.util.UUID;

public record CharacterAssignmentDto(
        UUID characterId,
        UUID userId, // Can be null if not yet assigned
        String inviteCode) { // Will be null if already assigned

}
