package com.geraldsaccount.killinary.model.dto.output.shared;

import java.util.UUID;

public record UserDto(
        UUID uuid,
        String name,
        String avatarUrl) {

}
