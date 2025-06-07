package com.geraldsaccount.killinary.model.dto.input;

public record CreateCharacterDto(
        String name,
        String shopDescription,
        String privateDescription,
        String avatarUrl) {

}
