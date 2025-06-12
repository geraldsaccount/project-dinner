package com.geraldsaccount.killinary.model.dto.input;

public record CreateCharacterDto(
        Integer index,
        String name,
        String shopDescription,
        String privateDescription,
        String avatarUrl) {

}
