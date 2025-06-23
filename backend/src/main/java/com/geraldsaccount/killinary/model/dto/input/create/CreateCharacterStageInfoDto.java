package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;

public record CreateCharacterStageInfoDto(
        String stageId,
        String objectivePrompt,
        List<CreateStageEvent> events) {

}
