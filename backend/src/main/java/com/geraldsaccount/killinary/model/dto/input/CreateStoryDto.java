package com.geraldsaccount.killinary.model.dto.input;

import java.util.Set;

import com.geraldsaccount.killinary.model.dto.input.create.CreateConfigDto;

public record CreateStoryDto(
        String title,
        String thumbnailDescription,
        String shopDescription,
        String dinnerStoryBrief,
        String bannerUrl,
        Set<CreateCharacterDto> characters,
        Set<CreateConfigDto> configs) {

}
