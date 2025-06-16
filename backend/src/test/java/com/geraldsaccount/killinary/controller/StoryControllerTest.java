package com.geraldsaccount.killinary.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.KillinaryApplication;
import com.geraldsaccount.killinary.TestDatabaseResetUtil;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCharacterStageInfoDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateCrimeDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageDto;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStageEvent;
import com.geraldsaccount.killinary.model.dto.input.create.CreateStoryDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Gender;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.PlayerConfigRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private MysteryRepository mysteryRepository;
    @Autowired
    private PlayerConfigRepository playerConfigRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDatabaseResetUtil databaseResetUtil;

    @Transactional
    void setUp() {
        Story story = Story.builder()
                .title("Test Story")
                .shopDescription("A thrilling adventure")
                .build();

        Character character1 = characterRepository.save(Character.builder()
                .name("Alice")
                .gender(Gender.FEMALE)
                .build());
        Character character2 = characterRepository.save(Character.builder()
                .name("Bob")
                .gender(Gender.MALE)
                .build());

        PlayerConfig config = PlayerConfig.builder()
                .characters(Set.of(character1, character2))
                .build();

        Mystery mystery = mysteryRepository.save(Mystery.builder()
                .story(story)
                .characters(List.of(character1, character2))
                .setups(List.of(config))
                .build());
    }

    @Test
    void getStorySummaries_returnsSummaries() throws Exception {
        setUp();
        mockMvc.perform(get("/api/stories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].story.title").value("Test Story"))
                .andExpect(jsonPath("$[0].story.thumbnailDescription").value("A thrilling adventure"))
                .andExpect(jsonPath("$[0].minPlayerCount").value(2))
                .andExpect(jsonPath("$[0].maxPlayerCount").value(2))
                .andExpect(jsonPath("$[0].configs[0].playerCount").value(2))
                .andExpect(jsonPath("$[0].configs[0].characterIds[0]").exists());
    }

    @Test
    void getStorySummaries_returnsEmpty_whenNoStories() throws Exception {
        mockMvc.perform(get("/api/stories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void createStory_createsStoryWithCharactersAndConfigs() throws Exception {
        CreateCharacterDto charDto1 = CreateCharacterDto.builder()
                .id("C1")
                .name("Alice")
                .role("Crazy Victorian Lady")
                .age(16)
                .isPrimary(true)
                .gender(Gender.FEMALE)
                .shopDescription("Description")
                .privateDescription("Secret")
                .avatarUrl("avatar.com")
                .build();

        CreateCharacterDto charDto2 = CreateCharacterDto.builder()
                .id("C2")
                .name("Bob")
                .role("Builder")
                .age(25)
                .isPrimary(false)
                .gender(Gender.MALE)
                .shopDescription("Description")
                .privateDescription("Secret")
                .avatarUrl("avatar.com")
                .build();

        CreateStageDto stageDto = new CreateStageDto("S1", 0, "The Beginning of the end",
                "Tell the guest whats going on");

        Map<String, String> relationships1 = Map.of(
                "C2", "Friend");
        Map<String, String> relationships2 = Map.of(
                "C1", "Friend");

        List<CreateStageEvent> stageEvents1 = List.of(
                new CreateStageEvent("S1", 0, "12:00", "Meet", "Meet at the park"));

        List<CreateStageEvent> stageEvents2 = List.of(
                new CreateStageEvent("S1", 0, "12:00", "Meet", "Meet at the park"));

        List<CreateCharacterStageInfoDto> stageInfo1 = List.of(
                new CreateCharacterStageInfoDto(stageDto.id(), "Find the clue", stageEvents1));

        List<CreateCharacterStageInfoDto> stageInfo2 = List.of(
                new CreateCharacterStageInfoDto(stageDto.id(), "Help Alice", stageEvents2));

        charDto1 = charDto1.withRelationships(relationships1).withStageInfo(stageInfo1);
        charDto2 = charDto2.withRelationships(relationships2).withStageInfo(stageInfo2);

        CreateConfigDto configDto = new CreateConfigDto("S1", 2, List.of(charDto1.id(), charDto2.id()));
        CreateStoryDto storyDto = new CreateStoryDto(
                "New Story",
                "Shop desc",
                "banner.png",
                "rules",
                "Story setting",
                "Dinner brief");
        CreateCrimeDto crimeDto = new CreateCrimeDto(List.of(charDto1.id()), "She did it frfr");
        CreateMysteryDto mysteryDto = new CreateMysteryDto(storyDto, List.of(charDto1, charDto2), List.of(stageDto),
                List.of(configDto),
                crimeDto);

        String requestBody = objectMapper.writeValueAsString(mysteryDto);
        mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

}
