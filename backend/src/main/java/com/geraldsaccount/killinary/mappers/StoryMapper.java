package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.dto.input.create.CreateStoryDto;
import com.geraldsaccount.killinary.model.mystery.Story;

@Component
public class StoryMapper {
    public Story asEntity(CreateStoryDto data) {
        return Story.builder()
                .title(data.title())
                .shopDescription(data.shopDescription())
                .bannerUrl(data.bannerUrl())
                .rules(data.rules())
                .setting(data.setting())
                .briefing(data.briefing())
                .build();

    }
}
