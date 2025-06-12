package com.geraldsaccount.killinary.controller;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.dto.input.CreateCharacterDto;
import com.geraldsaccount.killinary.model.dto.input.CreateConfigDto;
import com.geraldsaccount.killinary.model.dto.input.CreateStoryDto;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
class StoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private StoryConfigurationRepository storyConfigurationRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDatabaseResetUtil databaseResetUtil;

    @BeforeEach
    @Transactional
    void setUp() {
        databaseResetUtil.resetDatabase();

        Story story = storyRepository.save(Story.builder()
                .title("Test Story")
                .thumbnailDescription("A thrilling adventure")
                .build());

        Character character1 = characterRepository.save(Character.builder()
                .name("Alice")
                .gender(Gender.FEMALE)
                .story(story)
                .build());
        Character character2 = characterRepository.save(Character.builder()
                .name("Bob")
                .gender(Gender.MALE)
                .story(story)
                .build());

        StoryConfiguration config = storyConfigurationRepository.save(StoryConfiguration.builder()
                .characters(Set.of(character1, character2))
                .story(story)
                .build());

        storyRepository.save(story.withCharacters(Set.of(character1, character2))
                .withConfigurations(Set.of(config)));
    }

    @Test
    void getStorySummaries_returnsSummaries() throws Exception {
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
        databaseResetUtil.resetDatabase();
        mockMvc.perform(get("/api/stories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void createStory_createsStoryWithCharactersAndConfigs() throws Exception {
        var charDto1 = new CreateCharacterDto(0, "Alice", Gender.FEMALE, "desc", "private", "url1");
        var charDto2 = new CreateCharacterDto(1, "Bob", Gender.MALE, "desc2", "private2", "url2");
        var configDto = new CreateConfigDto(java.util.List.of(0, 1));
        var dto = new CreateStoryDto(
                "New Story",
                "A new adventure",
                "Shop desc",
                "Dinner brief",
                "banner.png",
                Set.of(charDto1, charDto2),
                Set.of(configDto));
        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void createStory_returnsBadRequest_whenConfigReferencesUnknownCharacterIndex() throws Exception {
        var charDto = new CreateCharacterDto(0, "Alice", Gender.FEMALE, "desc", "private", "url1");
        var configDto = new CreateConfigDto(List.of(1));
        var dto = new CreateStoryDto(
                "Invalid Story",
                "desc",
                "shop",
                "brief",
                "banner",
                Set.of(charDto),
                Set.of(configDto));
        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

}
