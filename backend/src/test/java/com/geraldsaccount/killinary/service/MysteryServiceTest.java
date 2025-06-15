package com.geraldsaccount.killinary.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.MysteryCreationException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.StoryMapper;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCrimeDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStoryDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Crime;
import com.geraldsaccount.killinary.model.mystery.Gender;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Stage;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class MysteryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private StoryConfigurationRepository configRepository;

    // @Mock
    // private StoryConfigMapper configMapper;

    @Mock
    private CharacterMapper characterMapper;

    @Mock
    private MysteryRepository mysteryRepository;

    @Mock
    private StoryMapper storyMapper;

    @InjectMocks
    private MysteryService mysteryService;

    private UUID charId;
    private UUID stageId;
    private UUID configId;
    private CreateStoryDto baseStoryDto;
    private CreateStageDto baseStageDto;
    private CreateCharacterDto baseCharacterDto;
    private CreateConfigDto baseConfigDto;
    private CreateCrimeDto baseCrimeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        charId = UUID.randomUUID();
        stageId = UUID.randomUUID();
        configId = UUID.randomUUID();
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
                storyRepository,
                characterRepository,
                configRepository,
                new StoryMapper(),
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

    // @Test
    // void getStorySummaries_returnsEmptySet_whenNoStories() {
    // when(storyRepository.findAll()).thenReturn(List.of());

    // Set<StoryForCreationDto> result = storyService.getStorySummaries();

    // assertThat(result).isEmpty();
    // }

    // @Test
    // void getStorySummaries_returnsSummaryWithCorrectMinMaxPlayersAndConfigs() {
    // PlayerConfig config1 = mock(PlayerConfig.class);
    // PlayerConfig config2 = mock(PlayerConfig.class);

    // // Use named mocks for characters to clarify intent and improve readability
    // Character charA = mock(Character.class, "charA");
    // Character charB = mock(Character.class, "charB");
    // Character charC = mock(Character.class, "charC");
    // Character charD = mock(Character.class, "charD");
    // Character charE = mock(Character.class, "charE");

    // when(config1.getCharacters()).thenReturn(Set.of(charA, charB, charC)); // 3
    // players
    // when(config2.getCharacters()).thenReturn(Set.of(charA, charB, charC, charD,
    // charE)); // 5 players

    // UUID storyId = UUID.randomUUID();
    // Story story = mock(Story.class);
    // when(story.getId()).thenReturn(storyId);
    // when(story.getTitle()).thenReturn("Test Story");
    // when(story.getConfigurations()).thenReturn(Set.of(config1, config2));

    // ConfigDto summaryDTO1 = mock(ConfigDto.class);
    // ConfigDto summaryDTO2 = mock(ConfigDto.class);

    // when(configMapper.asSummaryDTO(eq(config1))).thenReturn(summaryDTO1);
    // when(configMapper.asSummaryDTO(eq(config2))).thenReturn(summaryDTO2);

    // when(storyRepository.findAll()).thenReturn(List.of(story));

    // Set<StoryForCreationDto> result = storyService.getStorySummaries();

    // assertThat(result).hasSize(1);
    // StoryForCreationDto dto = result.iterator().next();
    // assertThat(dto.story().uuid()).isEqualTo(storyId);
    // assertThat(dto.story().title()).isEqualTo("Test Story");
    // assertThat(dto.minPlayerCount()).isEqualTo(3);
    // assertThat(dto.maxPlayerCount()).isEqualTo(5);
    // assertThat(dto.configs()).containsExactlyInAnyOrder(summaryDTO1,
    // summaryDTO2);
    // }

    // @Test
    // void getStoryByIdOrThrow_returnsStory_whenStoryExists() throws Exception {
    // UUID storyId = UUID.randomUUID();
    // Story story = mock(Story.class);
    // when(storyRepository.findById(storyId)).thenReturn(Optional.of(story));

    // Story result = storyService.getStoryOrThrow(storyId);

    // assertThat(result).isSameAs(story);
    // verify(storyRepository).findById(storyId);
    // }

    // @Test
    // void getStoryByIdOrThrow_throwsException_whenStoryDoesNotExist() {
    // UUID storyId = UUID.randomUUID();
    // when(storyRepository.findById(storyId)).thenReturn(Optional.empty());

    // org.assertj.core.api.ThrowableAssert.ThrowingCallable call = () ->
    // storyService.getStoryOrThrow(storyId);

    // assertThatThrownBy(() -> {
    // storyService.getStoryOrThrow(storyId);
    // }).isInstanceOf(StoryNotFoundException.class)
    // .hasMessageContaining("Could not find Story.");
    // verify(storyRepository).findById(storyId);
    // }

    @Test
    void createMystery_savesMystery_whenCalled() throws Exception {
        mysteryService = createServiceWithRealMappers();
        Story story = mock(Story.class);
        Character character = mock(Character.class);
        Stage stage = mock(Stage.class);
        PlayerConfig playerConfig = mock(PlayerConfig.class);
        Crime crime = mock(Crime.class);
        Mystery mystery = mock(Mystery.class);
        when(mysteryRepository.save(any(Mystery.class))).thenReturn(mystery);

        mysteryService.createMystery(buildValidMysteryDto());
        verify(mysteryRepository).save(any(Mystery.class));
    }

    @Test
    void createMystery_throwsMysteryCreation_whenCharacterIdIsNull() {
        mysteryService = createServiceWithRealMappers();
        CreateCharacterDto invalidCharacter = new CreateCharacterDto(
                null,
                baseCharacterDto.name(),
                baseCharacterDto.role(),
                baseCharacterDto.age(),
                baseCharacterDto.isPrimary(),
                baseCharacterDto.gender(),
                baseCharacterDto.shopDescription(),
                baseCharacterDto.privateDescription(),
                baseCharacterDto.avatarUrl(),
                baseCharacterDto.relationships(),
                baseCharacterDto.stageInfo());
        CreateMysteryDto dto = buildValidMysteryDto().withCharacters(List.of(invalidCharacter));

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenConfigHasNoValidCharacters() {
        mysteryService = createServiceWithRealMappers();
        UUID fakeCharId = UUID.randomUUID();
        CreateConfigDto invalidConfig = new CreateConfigDto(UUID.randomUUID(), 1, List.of(fakeCharId));
        CreateCrimeDto invalidCrime = new CreateCrimeDto(List.of(fakeCharId), "desc");
        CreateMysteryDto dto = buildValidMysteryDto()
                .withSetups(List.of(invalidConfig))
                .withCrime(invalidCrime);

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsIllegalArgument_whenInputIsNull() {
        mysteryService = createServiceWithRealMappers();
        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenStageIdIsNull() {
        mysteryService = createServiceWithRealMappers();
        CreateStageDto invalidStage = new CreateStageDto(null, 1, "Stage 1", "Prompt");
        CreateMysteryDto dto = buildValidMysteryDto().withStages(List.of(invalidStage));

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenStageInfoIsNull() {
        mysteryService = createServiceWithRealMappers();
        CreateCharacterDto invalidCharacter = new CreateCharacterDto(
                baseCharacterDto.id(),
                baseCharacterDto.name(),
                baseCharacterDto.role(),
                baseCharacterDto.age(),
                baseCharacterDto.isPrimary(),
                baseCharacterDto.gender(),
                baseCharacterDto.shopDescription(),
                baseCharacterDto.privateDescription(),
                baseCharacterDto.avatarUrl(),
                baseCharacterDto.relationships(),
                null // stageInfo
        );
        CreateMysteryDto dto = buildValidMysteryDto().withCharacters(List.of(invalidCharacter));

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenConfigIdIsNull() {
        mysteryService = createServiceWithRealMappers();
        CreateConfigDto invalidConfig = new CreateConfigDto(null, 1, List.of(baseCharacterDto.id()));
        CreateMysteryDto dto = buildValidMysteryDto().withSetups(List.of(invalidConfig));

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenConfigCharacterIdsIsNullOrEmpty() {
        mysteryService = createServiceWithRealMappers();
        CreateConfigDto configNull = new CreateConfigDto(UUID.randomUUID(), 1, null);
        CreateConfigDto configEmpty = new CreateConfigDto(UUID.randomUUID(), 1, List.of());
        CreateMysteryDto dtoNull = buildValidMysteryDto().withSetups(List.of(configNull));
        CreateMysteryDto dtoEmpty = buildValidMysteryDto().withSetups(List.of(configEmpty));

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dtoNull))
                .isInstanceOf(MysteryCreationException.class);
        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dtoEmpty))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenCrimeIsNull() {
        mysteryService = createServiceWithRealMappers();
        CreateMysteryDto dto = buildValidMysteryDto().withCrime(null);

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsNullPointer_whenStoryIsNull() {
        mysteryService = createServiceWithRealMappers();
        CreateMysteryDto dto = buildValidMysteryDto().withStory(null);

        assertThatThrownBy(() -> createServiceWithRealMappers().createMystery(dto))
                .isInstanceOf(NullPointerException.class);
    }
}
