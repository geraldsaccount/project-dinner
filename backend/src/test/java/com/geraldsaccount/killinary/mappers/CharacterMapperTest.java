package com.geraldsaccount.killinary.mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Gender;

class CharacterMapperTest {

    private final CharacterMapper characterMapper = new CharacterMapper();

    @Test
    void asSummaryDTO_shouldMapAllFields() {
        Character character = Character.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .shopDescription("A brave hero")
                .gender(Gender.MALE)
                .build();

        CharacterSummaryDto dto = characterMapper.asSummaryDTO(character);

        assertThat(dto.uuid()).isEqualTo(character.getId());
        assertThat(dto.name()).isEqualTo(character.getName());
        assertThat(dto.avatarUrl()).isEqualTo(character.getAvatarUrl());
    }

    @Test
    void asSummaryDTO_shouldHandleNullFields() {
        Character character = Character.builder()
                .id(null)
                .name(null)
                .shopDescription(null)
                .gender(null)
                .build();

        CharacterSummaryDto dto = characterMapper.asSummaryDTO(character);

        assertThat(dto.uuid()).isNull();
        assertThat(dto.name()).isNull();
        assertThat(dto.avatarUrl()).isNull();
    }
}
