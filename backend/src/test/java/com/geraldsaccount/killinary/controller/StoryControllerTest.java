package com.geraldsaccount.killinary.controller;

import java.util.List;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.KillinaryApplication;
import com.geraldsaccount.killinary.TestDatabaseResetUtil;
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

    @Test
    void getStorySummaries_returnsSummaries() throws Exception {
        saveMystery();
        mockMvc.perform(get("/api/mysteries").accept(MediaType.APPLICATION_JSON))
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
        mockMvc.perform(get("/api/mysteries").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Transactional
    void saveMystery() {
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
}
