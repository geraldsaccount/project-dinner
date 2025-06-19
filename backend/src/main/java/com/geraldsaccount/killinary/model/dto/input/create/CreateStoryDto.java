package com.geraldsaccount.killinary.model.dto.input.create;

public record CreateStoryDto(
        String title,
        String shopDescription,
        String bannerImage,
        String rules,
        String setting,
        String briefing) {

}
