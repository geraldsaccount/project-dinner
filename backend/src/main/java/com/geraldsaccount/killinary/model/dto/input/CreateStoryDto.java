package com.geraldsaccount.killinary.model.dto.input;

import java.util.Set;

public record CreateStoryDto(
        String title,
        String thumbnailDescription,
        String shopDescription,
        String dinnerStoryBrief,
        String bannerUrl,
        int minPlayerCount,
        int maxPlayerCount,
        Set<CreateCharacterDto> characters) {

}
