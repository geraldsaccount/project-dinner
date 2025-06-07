package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;

@Component
public class CharacterMapper {
    public CharacterSummaryDto asSummaryDTO(Character input) {
        return new CharacterSummaryDto(input.getId(), input.getName(), input.getAvatarUrl());
    }

    public CharacterDetailDto asDetailDTO(Character input) {
        return new CharacterDetailDto(input.getId(), input.getName(), input.getShopDescription(), input.getAvatarUrl());
    }
}
