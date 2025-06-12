package com.geraldsaccount.killinary.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.StoryConfigurationCreationException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryConfigMapper;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.dto.input.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.CreateStoryDto;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private StoryConfigurationRepository configRepository;

    @Mock
    private StoryConfigMapper configMapper;

    @Mock
    private CharacterMapper characterMapper;

    @InjectMocks
    private StoryService storyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStorySummaries_returnsEmptySet_whenNoStories() {
        when(storyRepository.findAll()).thenReturn(List.of());

        Set<StoryForCreationDto> result = storyService.getStorySummaries();

        assertThat(result).isEmpty();
    }

    @Test
    void getStorySummaries_returnsSummaryWithCorrectMinMaxPlayersAndConfigs() {
        StoryConfiguration config1 = mock(StoryConfiguration.class);
        StoryConfiguration config2 = mock(StoryConfiguration.class);

        // Use named mocks for characters to clarify intent and improve readability
        Character charA = mock(Character.class, "charA");
        Character charB = mock(Character.class, "charB");
        Character charC = mock(Character.class, "charC");
        Character charD = mock(Character.class, "charD");
        Character charE = mock(Character.class, "charE");

        when(config1.getCharacters()).thenReturn(Set.of(charA, charB, charC)); // 3 players
        when(config2.getCharacters()).thenReturn(Set.of(charA, charB, charC, charD, charE)); // 5 players

        UUID storyId = UUID.randomUUID();
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(storyId);
        when(story.getTitle()).thenReturn("Test Story");
        when(story.getConfigurations()).thenReturn(Set.of(config1, config2));

        ConfigDto summaryDTO1 = mock(ConfigDto.class);
        ConfigDto summaryDTO2 = mock(ConfigDto.class);

        when(configMapper.asSummaryDTO(eq(config1))).thenReturn(summaryDTO1);
        when(configMapper.asSummaryDTO(eq(config2))).thenReturn(summaryDTO2);

        when(storyRepository.findAll()).thenReturn(List.of(story));

        Set<StoryForCreationDto> result = storyService.getStorySummaries();

        assertThat(result).hasSize(1);
        StoryForCreationDto dto = result.iterator().next();
        assertThat(dto.story().uuid()).isEqualTo(storyId);
        assertThat(dto.story().title()).isEqualTo("Test Story");
        assertThat(dto.minPlayerCount()).isEqualTo(3);
        assertThat(dto.maxPlayerCount()).isEqualTo(5);
        assertThat(dto.configs()).containsExactlyInAnyOrder(summaryDTO1, summaryDTO2);
    }

    @Test
    void getStoryByIdOrThrow_returnsStory_whenStoryExists() throws Exception {
        UUID storyId = UUID.randomUUID();
        Story story = mock(Story.class);
        when(storyRepository.findById(storyId)).thenReturn(Optional.of(story));

        Story result = storyService.getStoryOrThrow(storyId);

        assertThat(result).isSameAs(story);
        verify(storyRepository).findById(storyId);
    }

    @Test
    void getStoryByIdOrThrow_throwsException_whenStoryDoesNotExist() {
        UUID storyId = UUID.randomUUID();
        when(storyRepository.findById(storyId)).thenReturn(Optional.empty());

        org.assertj.core.api.ThrowableAssert.ThrowingCallable call = () -> storyService.getStoryOrThrow(storyId);

        assertThatThrownBy(() -> {
            storyService.getStoryOrThrow(storyId);
        }).isInstanceOf(StoryNotFoundException.class)
                .hasMessageContaining("Could not find Story.");
        verify(storyRepository).findById(storyId);
    }

    @Test
    void createStory_savesStoryCharactersAndConfigsCorrectly() throws StoryConfigurationCreationException {
        // Arrange
        CreateCharacterDto charDto1 = new CreateCharacterDto(0, "Alice", Gender.FEMALE, "desc", "private", "url");
        CreateCharacterDto charDto2 = new CreateCharacterDto(1, "Bob", Gender.MALE, "desc2", "private2", "url2");

        CreateConfigDto configDto = new CreateConfigDto(List.of(0, 1));

        CreateStoryDto input = new CreateStoryDto(
                "My Story",
                "desc",
                "shop",
                "brief",
                "banner",
                Set.of(charDto1, charDto2),
                Set.of(configDto));

        Story savedStory = mock(Story.class);
        when(storyRepository.save(any(Story.class))).thenReturn(savedStory);
        Character character1 = Character.builder()
                .id(UUID.randomUUID())
                .name("Alice")
                .shopDescription("Desc")
                .privateBriefing("Private")
                .avatarUrl("URL")
                .build();
        Character character2 = Character.builder()
                .id(UUID.randomUUID())
                .name("Bob")
                .shopDescription("Desc")
                .privateBriefing("Private")
                .avatarUrl("URL")
                .build();

        when(characterMapper.asCharacter(charDto1)).thenReturn(character1);
        when(characterMapper.asCharacter(charDto2)).thenReturn(character2);
        // Only mock repositories and services, not mappers or models
        when(characterRepository.saveAll(anySet()))
                .thenReturn(List.of(character1, character2));
        when(configRepository.saveAll(anySet()))
                .thenReturn(List.of());

        // Simulate withCharacters and withConfigurations returning the story itself
        when(savedStory.withCharacters(anySet())).thenReturn(savedStory);
        when(savedStory.withConfigurations(anySet())).thenReturn(savedStory);

        // Act
        storyService.createStory(input);

        // Assert
        verify(storyRepository, times(2)).save(any(Story.class));
        verify(characterRepository).saveAll(anySet());
        verify(configRepository).saveAll(anySet());
        verify(storyRepository).save(savedStory);
    }

    @Test
    void createStory_throwsException_whenConfigReferencesUnknownCharacterIndex() {
        // Arrange
        CreateCharacterDto charDto = new CreateCharacterDto(0, "Alice", Gender.FEMALE, "desc", "private", "url");

        CreateConfigDto configDto = new CreateConfigDto(List.of(1)); // index 1 does not exist

        CreateStoryDto input = new CreateStoryDto(
                "My Story",
                "desc",
                "shop",
                "brief",
                "banner",
                Set.of(charDto),
                Set.of(configDto));

        Story savedStory = mock(Story.class);
        when(storyRepository.save(org.mockito.ArgumentMatchers.any(Story.class))).thenReturn(savedStory);

        Character character = Character.builder()
                .id(UUID.randomUUID())
                .name("Alice")
                .shopDescription("Desc")
                .privateBriefing("Private")
                .avatarUrl("URL")
                .build();
        when(characterMapper.asCharacter(charDto)).thenReturn(character);

        when(characterRepository.saveAll(anySet()))
                .thenReturn(List.of(character));

        // Simulate withCharacters and withConfigurations returning the story itself
        when(savedStory.withCharacters(anySet())).thenReturn(savedStory);
        when(savedStory.withConfigurations(anySet())).thenReturn(savedStory);

        // Act & Assert
        assertThatThrownBy(() -> storyService.createStory(input))
                .isInstanceOf(StoryConfigurationCreationException.class)
                .hasMessageContaining("referenced in configuration, not found for this story");
    }
}
