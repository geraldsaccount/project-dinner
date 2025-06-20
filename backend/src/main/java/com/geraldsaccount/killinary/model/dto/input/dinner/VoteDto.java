package com.geraldsaccount.killinary.model.dto.input.dinner;

import java.util.UUID;

import lombok.Builder;

@Builder
public record VoteDto(
        UUID suspectId,
        String motive) {

}
