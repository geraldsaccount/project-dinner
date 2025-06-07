package com.geraldsaccount.killinary.mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;

class CharacterMapperTest {

    private final CharacterMapper characterMapper = new CharacterMapper();

    @Test
    void asSummaryDTO_shouldMapAllFields() {
        Character character = new Character();
        character.setId(UUID.randomUUID());
        character.setName("John Doe");
        character.setShopDescription("A brave hero");
        character.setGender(Gender.MALE);

        CharacterSummaryDto dto = characterMapper.asSummaryDTO(character);

        assertThat(dto.uuid()).isEqualTo(character.getId());
        assertThat(dto.name()).isEqualTo(character.getName());
        assertThat(dto.avatarUrl()).isEqualTo(character.getAvatarUrl());
    }

    @Test
    void asSummaryDTO_shouldHandleNullFields() {
        Character character = new Character();
        character.setId(null);
        character.setName(null);
        character.setShopDescription(null);
        character.setGender(null);

        CharacterSummaryDto dto = characterMapper.asSummaryDTO(character);

        assertThat(dto.uuid()).isNull();
        assertThat(dto.name()).isNull();
        assertThat(dto.avatarUrl()).isNull();
    }
}
