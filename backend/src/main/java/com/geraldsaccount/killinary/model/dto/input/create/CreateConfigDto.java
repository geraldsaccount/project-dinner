package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;
import java.util.UUID;

public record CreateConfigDto(
        UUID id,
        Integer playerCount,
        List<UUID> characterIds) {

}
