package com.geraldsaccount.killinary.model.dto.input;

import com.geraldsaccount.killinary.model.mystery.Gender;

public record CreateCharacterDto(
        Integer index,
        String name,
        Gender gender,
        String shopDescription,
        String privateDescription,
        String avatarUrl) {

}
