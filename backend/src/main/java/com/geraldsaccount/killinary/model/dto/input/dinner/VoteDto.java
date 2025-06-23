package com.geraldsaccount.killinary.model.dto.input.dinner;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record VoteDto(
    List<UUID> suspectIds,
    String motive) {

}
