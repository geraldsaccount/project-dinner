package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.UUID;

public record CreateStageEvent(
        UUID id,
        Integer order,
        String time,
        String title,
        String description) {

}
