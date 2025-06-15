package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;
import com.geraldsaccount.killinary.model.mystery.Character;

@Component
public class CharacterMapper {
    public CharacterSummaryDto asSummaryDTO(Character input) {
        return new CharacterSummaryDto(input.getId(), input.getName(), input.getAvatarUrl(), input.getRole());
    }

    public CharacterDetailDto asDetailDTO(Character input) {
        return new CharacterDetailDto(input.getId(), input.getName(), input.getShopDescription(), input.getAvatarUrl(),
                input.getRole());
    }

    public Character asEntity(CreateCharacterDto create) {
        return Character.builder()
                .id(create.id())
                .name(create.name())
                .role(create.role())
                .age(create.age())
                .isPrimary(create.isPrimary())
                .gender(create.gender())
                .shopDescription(create.shopDescription())
                .privateDescription(create.privateDescription())
                .avatarUrl(create.avatarUrl())
                .relationships(create.relationships())
                .build();
    }
}
