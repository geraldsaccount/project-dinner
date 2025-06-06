package com.geraldsaccount.killinary.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryConfigMapper;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.dto.output.StoryConfigSummaryDTO;
import com.geraldsaccount.killinary.model.dto.output.StorySummaryDTO;
import com.geraldsaccount.killinary.repository.StoryRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class StoryServiceTest {

    private StoryRepository storyRepository;
    private StoryConfigMapper configMapper;
    private CharacterMapper characterMapper;
    private StoryService storyService;

    @BeforeEach
    void setUp() {
        storyRepository = mock(StoryRepository.class);
        configMapper = mock(StoryConfigMapper.class);
        characterMapper = mock(CharacterMapper.class);
        storyService = new StoryService(storyRepository, configMapper, characterMapper);
    }

    @Test
    void getStorySummaries_returnsEmptySet_whenNoStories() {
        when(storyRepository.findAll()).thenReturn(List.of());

        Set<StorySummaryDTO> result = storyService.getStorySummaries();

        assertThat(result).isEmpty();
    }

    @Test
    void getStorySummaries_returnsSummaryWithCorrectMinMaxPlayersAndConfigs() {
        StoryConfiguration config1 = mock(StoryConfiguration.class);
        StoryConfiguration config2 = mock(StoryConfiguration.class);

        when(config1.getPlayerCount()).thenReturn(3);
        when(config2.getPlayerCount()).thenReturn(5);

        UUID storyId = UUID.randomUUID();
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(storyId);
        when(story.getTitle()).thenReturn("Test Story");
        when(story.getConfigurations()).thenReturn(Set.of(config1, config2));

        StoryConfigSummaryDTO summaryDTO1 = mock(StoryConfigSummaryDTO.class);
        StoryConfigSummaryDTO summaryDTO2 = mock(StoryConfigSummaryDTO.class);

        when(configMapper.asSummaryDTO(eq(config1))).thenReturn(summaryDTO1);
        when(configMapper.asSummaryDTO(eq(config2))).thenReturn(summaryDTO2);

        when(storyRepository.findAll()).thenReturn(List.of(story));

        Set<StorySummaryDTO> result = storyService.getStorySummaries();

        assertThat(result).hasSize(1);
        StorySummaryDTO dto = result.iterator().next();
        assertThat(dto.id()).isEqualTo(storyId);
        assertThat(dto.title()).isEqualTo("Test Story");
        assertThat(dto.minPlayers()).isEqualTo(3);
        assertThat(dto.maxPlayers()).isEqualTo(5);
        assertThat(dto.configs()).containsExactlyInAnyOrder(summaryDTO1,
                summaryDTO2);
    }
}
