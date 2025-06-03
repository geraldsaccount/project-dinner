package com.geraldsaccount.killinary.mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.dto.output.CharacterSummaryDTO;

class CharacterMapperTest {

    private final CharacterMapper characterMapper = new CharacterMapper();

    @Test
    void asSummaryDTO_shouldMapAllFields() {
        Character character = new Character();
        character.setId(UUID.randomUUID());
        character.setName("John Doe");
        character.setCharacterDescription("A brave hero");
        character.setGender(Gender.MALE);

        CharacterSummaryDTO dto = characterMapper.asSummaryDTO(character);

        assertThat(dto.id()).isEqualTo(character.getId());
        assertThat(dto.name()).isEqualTo(character.getName());
        assertThat(dto.characterDescription()).isEqualTo(character.getCharacterDescription());
        assertThat(dto.gender()).isEqualTo(character.getGender());
    }

    @Test
    void asSummaryDTO_shouldHandleNullFields() {
        Character character = new Character();
        character.setId(null);
        character.setName(null);
        character.setCharacterDescription(null);
        character.setGender(null);

        CharacterSummaryDTO dto = characterMapper.asSummaryDTO(character);

        assertThat(dto.id()).isNull();
        assertThat(dto.name()).isNull();
        assertThat(dto.characterDescription()).isNull();
        assertThat(dto.gender()).isNull();
    }
}
