package com.geraldsaccount.killinary.model.dto.input.create;

public record CreateStageEvent(
        String id,
        Integer order,
        String time,
        String title,
        String description) {

}
