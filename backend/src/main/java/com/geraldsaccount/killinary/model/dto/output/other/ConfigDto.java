package com.geraldsaccount.killinary.model.dto.output.other;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.geraldsaccount.killinary.model.Gender;

public record ConfigDto(
        UUID id,
        Integer playerCount,
        Map<Gender, Integer> genderCounts,
        Set<UUID> characterIds) {

}
