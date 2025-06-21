package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.utils.ImageConverter;

@Component
public class CharacterMapper {
    public CharacterSummaryDto asSummaryDTO(Character input) {
        String avatarBase64 = ImageConverter.imageAsBase64(input.getAvatarImage());

        return new CharacterSummaryDto(input.getId(), input.getName(), avatarBase64, input.getRole());
    }

    public CharacterDetailDto asDetailDTO(Character input) {
        String avatarBase64 = ImageConverter.imageAsBase64(input.getAvatarImage());

        return new CharacterDetailDto(
                input.getId(),
                input.getName(),
                input.getAge(),
                input.getShopDescription(),
                avatarBase64,
                input.getRole());
    }

    public Character asEntity(CreateCharacterDto create) {
        return Character.builder()
                .name(create.name())
                .role(create.role())
                .age(create.age())
                .isPrimary(create.isPrimary())
                .gender(create.gender())
                .shopDescription(create.shopDescription())
                .privateDescription(create.privateDescription())
                .build();
    }
}
