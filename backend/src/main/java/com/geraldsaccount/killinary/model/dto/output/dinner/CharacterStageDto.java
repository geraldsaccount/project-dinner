package com.geraldsaccount.killinary.model.dto.output.dinner;

import java.util.List;

public record CharacterStageDto(
        String stageTitle,
        String objectiveDesc,
        List<StageEventDto> events) {

}
