package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.dto.input.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;

@Component
public class CharacterMapper {
    public CharacterSummaryDto asSummaryDTO(Character input) {
        return new CharacterSummaryDto(input.getId(), input.getName(), input.getAvatarUrl(), input.getRole());
    }

    public CharacterDetailDto asDetailDTO(Character input) {
        return new CharacterDetailDto(input.getId(), input.getName(), input.getShopDescription(), input.getAvatarUrl(),
                input.getRole());
    }

    public Character asCharacter(CreateCharacterDto create) {
        return Character.builder()
                .name(create.name())
                .shopDescription(create.shopDescription())
                .privateBriefing(create.privateDescription())
                .avatarUrl(create.avatarUrl())
                .build();
    }
}
