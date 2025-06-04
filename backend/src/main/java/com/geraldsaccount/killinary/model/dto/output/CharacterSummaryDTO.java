package com.geraldsaccount.killinary.model.dto.output;

import java.util.UUID;

import com.geraldsaccount.killinary.model.Gender;

import lombok.Builder;

@Builder
public record CharacterSummaryDTO(UUID id,
        String name,
        String characterDescription,
        Gender gender) {

}
