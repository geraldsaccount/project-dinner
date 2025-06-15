package com.geraldsaccount.killinary.model.dto.input.create;

public record CreateStoryDto(
        String title,
        String shopDescription,
        String bannerUrl,
        String rules,
        String setting,
        String briefing) {

}
