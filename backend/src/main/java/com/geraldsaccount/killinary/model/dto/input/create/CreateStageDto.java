package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.UUID;

public record CreateStageDto(
        UUID id,
        Integer order,
        String title,
        String hostPrompt) {

}
