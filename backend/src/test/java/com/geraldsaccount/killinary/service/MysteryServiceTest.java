package com.geraldsaccount.killinary.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.MysteryNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.PlayerConfigMapper;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCrimeDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStoryDto;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Gender;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.StageRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class MysteryServiceTest {

    @Mock
    private StoryRepository storyRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private StageRepository stageRepository;

    @Mock
    private MysteryRepository mysteryRepository;

    @InjectMocks
    private MysteryService mysteryService;

    private String charId;
    private String stageId;
    private String configId;
    private CreateStoryDto baseStoryDto;
    private CreateStageDto baseStageDto;
    private CreateCharacterDto baseCharacterDto;
    private CreateConfigDto baseConfigDto;
    private CreateCrimeDto baseCrimeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        charId = "C1";
        stageId = "S1";
        configId = "Conf1";
        baseStoryDto = new CreateStoryDto(
                "title",
                "shopDescription",
                "bannerUrl",
                "rules",
                "setting",
                "briefing");
        baseStageDto = new CreateStageDto(stageId, 1, "Stage 1", "Prompt");
        baseCharacterDto = new CreateCharacterDto(
                charId,
                "CharName",
                "role",
                30,
                true,
                Gender.MALE,
                "shopDescription",
                "privateDescription",
                "avatarUrl",
                Collections.emptyMap(),
                Collections.emptyList());
        baseConfigDto = new CreateConfigDto(configId, 1, List.of(charId));
        baseCrimeDto = new CreateCrimeDto(List.of(charId), "desc");
    }

    private MysteryService createServiceWithRealMappers() {
        return new MysteryService(
                mysteryRepository,
                new PlayerConfigMapper(),
                new CharacterMapper());
    }

    private CreateMysteryDto buildValidMysteryDto() {
        return new CreateMysteryDto(
                baseStoryDto,
                List.of(baseCharacterDto),
                List.of(baseStageDto),
                List.of(baseConfigDto),
                baseCrimeDto);
    }

    @Test
    void getStorySummaries_returnsEmptySet_whenNoStories() {
        when(storyRepository.findAll()).thenReturn(List.of());

        Set<StoryForCreationDto> result = mysteryService.getMysterySummaries();

        assertThat(result).isEmpty();
    }

    @Test
    void getStorySummaries_returnsSummaryWithCorrectMinMaxPlayersAndConfigs() {
        PlayerConfig config1 = mock(PlayerConfig.class);
        PlayerConfig config2 = mock(PlayerConfig.class);

        // Use named mocks for characters to clarify intent and improve readability
        Character charA = mock(Character.class, "charA");
        Character charB = mock(Character.class, "charB");
        Character charC = mock(Character.class, "charC");
        Character charD = mock(Character.class, "charD");
        Character charE = mock(Character.class, "charE");

        when(config1.getCharacters()).thenReturn(Set.of(charA, charB, charC));
        when(config2.getCharacters()).thenReturn(Set.of(charA, charB, charC, charD, charE));

        UUID mysteryId = UUID.randomUUID();
        Story story = mock(Story.class);
        when(story.getTitle()).thenReturn("Test Story");
        Mystery mystery = new Mystery();
        mystery.setId(mysteryId);
        mystery.setStory(story);
        mystery.setSetups(List.of(config1, config2));

        ConfigDto summaryDTO1 = mock(ConfigDto.class);
        ConfigDto summaryDTO2 = mock(ConfigDto.class);

        PlayerConfigMapper configMapper = mock(PlayerConfigMapper.class);

        when(configMapper.asSummaryDTO(config1)).thenReturn(summaryDTO1);
        when(configMapper.asSummaryDTO(config2)).thenReturn(summaryDTO2);
        when(mysteryRepository.findAll()).thenReturn(List.of(mystery));

        mysteryService = new MysteryService(
                mysteryRepository,
                configMapper,
                new CharacterMapper());
        Set<StoryForCreationDto> result = mysteryService.getMysterySummaries();

        assertThat(result).hasSize(1);
        StoryForCreationDto dto = result.iterator().next();
        assertThat(dto.uuid()).isEqualTo(mysteryId);
        assertThat(dto.story().title()).isEqualTo("Test Story");
        assertThat(dto.minPlayerCount()).isEqualTo(3);
        assertThat(dto.maxPlayerCount()).isEqualTo(5);
        assertThat(dto.configs()).containsExactlyInAnyOrder(summaryDTO1,
                summaryDTO2);
    }

    @Test
    void getStoryByIdOrThrow_returnsStory_whenStoryExists() throws Exception {
        UUID mysteryId = UUID.randomUUID();
        Mystery mystery = mock(Mystery.class);
        when(mysteryRepository.findById(mysteryId)).thenReturn(Optional.of(mystery));

        Mystery result = mysteryService.getMysteryOrThrow(mysteryId);

        assertThat(result).isSameAs(mystery);
        verify(mysteryRepository).findById(mysteryId);
    }

    @Test
    void getStoryByIdOrThrow_throwsException_whenStoryDoesNotExist() {
        UUID mysteryId = UUID.randomUUID();
        when(storyRepository.findById(mysteryId)).thenReturn(Optional.empty());

        org.assertj.core.api.ThrowableAssert.ThrowingCallable call = () -> mysteryService.getMysteryOrThrow(mysteryId);

        assertThatThrownBy(() -> {
            mysteryService.getMysteryOrThrow(mysteryId);
        }).isInstanceOf(MysteryNotFoundException.class)
                .hasMessageContaining("Could not find Story.");
        verify(mysteryRepository).findById(mysteryId);
    }
}
