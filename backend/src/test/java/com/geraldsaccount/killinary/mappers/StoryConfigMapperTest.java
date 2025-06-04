package com.geraldsaccount.killinary.mappers;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.StoryConfigurationCharacter;
import com.geraldsaccount.killinary.model.dto.output.StoryConfigSummaryDTO;

class StoryConfigMapperTest {

    private final StoryConfigMapper mapper = new StoryConfigMapper();

    @Test
    void asSummaryDTO_shouldMapFieldsCorrectly() {
        Character character1 = Character.builder()
                .id(UUID.randomUUID())
                .gender(Gender.MALE)
                .build();

        Character character2 = Character.builder()
                .id(UUID.randomUUID())
                .gender(Gender.FEMALE)
                .build();

        StoryConfiguration config = StoryConfiguration.builder()
                .id(UUID.randomUUID())
                .configurationName("Config")
                .playerCount(2)
                .build();

        StoryConfigurationCharacter confChar1 = new StoryConfigurationCharacter(config, character1);
        StoryConfigurationCharacter confChar2 = new StoryConfigurationCharacter(config, character2);

        config.setCharactersInConfig(Set.of(confChar1, confChar2));

        StoryConfigSummaryDTO dto = mapper.asSummaryDTO(config);

        assertThat(dto.id()).isEqualTo(config.getId());
        assertThat(dto.playerCount()).isEqualTo(2);
        assertThat(dto.characterIds()).hasSize(2)
                .containsExactlyInAnyOrder(character1.getId(), character2.getId());

        assertThat(dto.genderCounts())
                .containsEntry(Gender.MALE, 1)
                .containsEntry(Gender.FEMALE, 1);
    }

    @Test
    void asSummaryDTO_shouldHandleEmptyCharacters() {
        StoryConfiguration config = StoryConfiguration.builder()
                .id(UUID.randomUUID())
                .playerCount(0)
                .charactersInConfig(Set.of())
                .build();

        StoryConfigSummaryDTO dto = mapper.asSummaryDTO(config);

        assertThat(dto.id()).isEqualTo(config.getId());
        assertThat(dto.playerCount()).isEqualTo(0);
        assertThat(dto.characterIds()).isEmpty();
        assertThat(dto.genderCounts()).isEmpty();
    }

    @Test
    void asSummaryDTO_shouldCountMultipleSameGender() {
        Character character1 = Character.builder()
                .id(UUID.randomUUID())
                .gender(Gender.MALE)
                .build();

        Character character2 = Character.builder()
                .id(UUID.randomUUID())
                .gender(Gender.MALE)
                .build();

        StoryConfiguration config = StoryConfiguration.builder()
                .id(UUID.randomUUID())
                .playerCount(2)
                .build();

        StoryConfigurationCharacter confChar1 = new StoryConfigurationCharacter(config, character1);
        StoryConfigurationCharacter confChar2 = new StoryConfigurationCharacter(config, character2);

        config.setCharactersInConfig(Set.of(confChar1, confChar2));

        StoryConfigSummaryDTO dto = mapper.asSummaryDTO(config);

        assertThat(dto.genderCounts())
                .containsEntry(Gender.MALE, 2);
        assertThat(dto.genderCounts()).doesNotContainKey(Gender.FEMALE);
    }
}
