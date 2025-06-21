package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.UUID;

public record CharacterDetailDto(
        UUID uuid,
        String name,
        Integer age,
        String shopDescription,
        String avatarData,
        String role) {

}
