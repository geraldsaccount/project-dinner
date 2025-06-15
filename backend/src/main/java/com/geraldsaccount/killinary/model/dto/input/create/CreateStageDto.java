package com.geraldsaccount.killinary.model.dto.input.create;

public record CreateStageDto(
        String id,
        Integer order,
        String title,
        String hostPrompt) {

}
