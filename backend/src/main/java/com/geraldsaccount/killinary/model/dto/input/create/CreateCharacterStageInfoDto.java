package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;
import java.util.UUID;

public record CreateCharacterStageInfoDto(
        UUID stageId,
        String objectivePrompt,
        List<CreateStageEvent> stageEvents) {

}
