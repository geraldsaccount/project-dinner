package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.dto.output.CharacterSummaryDTO;

@Component
public class CharacterMapper {
    public CharacterSummaryDTO asSummaryDTO(Character input) {
        return CharacterSummaryDTO.builder()
                .id(input.getId())
                .name(input.getName())
                .characterDescription(input.getCharacterDescription())
                .gender(input.getGender())
                .build();
    }
}
