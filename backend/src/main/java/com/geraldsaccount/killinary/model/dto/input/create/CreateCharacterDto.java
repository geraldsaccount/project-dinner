package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.geraldsaccount.killinary.model.mystery.Gender;

public record CreateCharacterDto(
        UUID id,
        String name,
        String role,
        Integer age,
        boolean isPrimary,
        Gender gender,
        String shopDescription,
        String privateDescription,
        String avatarUrl,
        Map<UUID, String> relationships,
        List<CreateCharacterStageInfoDto> stageInfo) {

}
