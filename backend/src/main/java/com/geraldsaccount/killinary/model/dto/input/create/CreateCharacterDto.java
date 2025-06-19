package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;
import java.util.Map;

import com.geraldsaccount.killinary.model.mystery.Gender;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record CreateCharacterDto(
        String id,
        String name,
        String role,
        Integer age,
        boolean isPrimary,
        Gender gender,
        String shopDescription,
        String privateDescription,
        String avatarImage,
        Map<String, String> relationships,
        List<CreateCharacterStageInfoDto> stageInfo) {

}
