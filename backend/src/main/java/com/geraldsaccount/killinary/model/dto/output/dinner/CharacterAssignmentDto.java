package com.geraldsaccount.killinary.model.dto.output.dinner;

import java.util.Optional;
import java.util.UUID;

public record CharacterAssignmentDto(
        UUID characterId,
        Optional<UUID> userId, // Can be null if not yet assigned
        Optional<String> inviteCode) { // Will be null if already assigned

}
