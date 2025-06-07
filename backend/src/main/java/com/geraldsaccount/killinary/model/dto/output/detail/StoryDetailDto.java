package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.Set;
import java.util.UUID;

public record StoryDetailDto(
        UUID uuid,
        String title,
        String shopDescription,
        String bannerUrl,
        int minPlayerCount,
        int maxPlayerCount,
        Set<CharacterDetailDto> characters) {

}
