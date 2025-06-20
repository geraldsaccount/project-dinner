package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.geraldsaccount.killinary.model.dto.output.dinner.CharacterStageDto;

public record PrivateInfoDto(
        UUID characterId,
        String characterDescription,
        Map<UUID, String> relationships,
        List<CharacterStageDto> stages) {

}
