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
import com.geraldsaccount.killinary.model.mystery.Gender;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.Stage;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.StageRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
public class EditorServiceTest {
    @Mock
    private StoryRepository storyRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private StageRepository stageRepository;

    @Mock
    private MysteryRepository mysteryRepository;

    @InjectMocks
    private EditorService editorService;

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

    private EditorService createServiceWithRealMappers() {
        return new EditorService(
                mysteryRepository,
                characterRepository,
                stageRepository,
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

    @Test
    void createMystery_savesMystery_whenCalled() throws Exception {
        editorService = createServiceWithRealMappers();
        Character character = Character.builder()
                .id(UUID.randomUUID())
                .name("CharName")
                .build();
        Stage stage = Stage.builder()
                .id(UUID.randomUUID())
                .title("Stage 1")
                .build();
        when(characterRepository.saveAll(any())).thenReturn(List.of(character));
        when(stageRepository.saveAll(any())).thenReturn(List.of(stage));

        editorService.createMystery(buildValidMysteryDto());
        verify(mysteryRepository).save(any(Mystery.class));
    }

    @Test
    void createMystery_throwsMysteryCreation_whenCharacterIdIsNull() {
        editorService = createServiceWithRealMappers();
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
        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenConfigHasNoValidCharacters() {
        editorService = createServiceWithRealMappers();
        String fakeCharId = "FakeChar";
        CreateConfigDto invalidConfig = new CreateConfigDto("Conf2", 1, List.of(fakeCharId));
        CreateCrimeDto invalidCrime = new CreateCrimeDto(List.of(fakeCharId), "desc");
        CreateMysteryDto dto = buildValidMysteryDto()
                .withSetups(List.of(invalidConfig))
                .withCrime(invalidCrime);

        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsIllegalArgument_whenInputIsNull() {
        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenStageIdIsNull() {
        editorService = createServiceWithRealMappers();
        CreateStageDto invalidStage = new CreateStageDto(null, 1, "Stage 1", "Prompt");
        CreateMysteryDto dto = buildValidMysteryDto().withStages(List.of(invalidStage));

        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenStageInfoIsNull() {
        editorService = createServiceWithRealMappers();
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

        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenConfigIdIsNull() {
        editorService = createServiceWithRealMappers();
        CreateConfigDto invalidConfig = new CreateConfigDto(null, 1, List.of(baseCharacterDto.id()));
        CreateMysteryDto dto = buildValidMysteryDto().withSetups(List.of(invalidConfig));

        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenConfigCharacterIdsIsNullOrEmpty() {
        editorService = createServiceWithRealMappers();
        CreateConfigDto configNull = new CreateConfigDto("ConfA", 1, null);
        CreateConfigDto configEmpty = new CreateConfigDto("ConfB", 1, List.of());
        CreateMysteryDto dtoNull = buildValidMysteryDto().withSetups(List.of(configNull));
        CreateMysteryDto dtoEmpty = buildValidMysteryDto().withSetups(List.of(configEmpty));

        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dtoNull))
                .isInstanceOf(MysteryCreationException.class);
        assertThatThrownBy(() -> editorService.createMystery(dtoEmpty))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsMysteryCreation_whenCrimeIsNull() {
        editorService = createServiceWithRealMappers();
        CreateMysteryDto dto = buildValidMysteryDto().withCrime(null);

        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dto))
                .isInstanceOf(MysteryCreationException.class);
    }

    @Test
    void createMystery_throwsNullPointer_whenStoryIsNull() {
        editorService = createServiceWithRealMappers();
        CreateMysteryDto dto = buildValidMysteryDto().withStory(null);

        editorService = createServiceWithRealMappers();
        assertThatThrownBy(() -> editorService.createMystery(dto))
                .isInstanceOf(NullPointerException.class);
    }
}
