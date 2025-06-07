package com.geraldsaccount.killinary.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.KillinaryApplication;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.StoryConfigurationCharacter;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.SessionRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationCharacterRepository;
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
    private SessionRepository sessionRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private StoryConfigurationRepository storyConfigurationRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private StoryConfigurationCharacterRepository storyConfigurationCharacterRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @Transactional
    void setUp() {
        sessionRepository.deleteAll();
        storyConfigurationCharacterRepository.deleteAll();
        storyConfigurationRepository.deleteAll();
        characterRepository.deleteAll();
        storyRepository.deleteAll();

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
                .story(story)
                .playerCount(2)
                .configurationName("Default")
                .build());

        StoryConfigurationCharacter configCharacter1 = storyConfigurationCharacterRepository
                .save(new StoryConfigurationCharacter(config, character1));
        StoryConfigurationCharacter configCharacter2 = storyConfigurationCharacterRepository
                .save(new StoryConfigurationCharacter(config, character2));
        config = storyConfigurationRepository
                .save(config.withCharactersInConfig(Set.of(configCharacter1, configCharacter2)));

        storyRepository.save(story.withCharacters(Set.of(character1, character2))
                .withConfigurations(Set.of(config)));
    }

    @Test
    void getStorySummaries_returnsSummaries() throws Exception {
        mockMvc.perform(get("/api/stories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Story"))
                .andExpect(jsonPath("$[0].thumbnailDescription").value("A thrilling adventure"))
                .andExpect(jsonPath("$[0].minPlayerCount").value(2))
                .andExpect(jsonPath("$[0].maxPlayerCount").value(2))
                .andExpect(jsonPath("$[0].configs[0].playerCount").value(2))
                .andExpect(jsonPath("$[0].configs[0].characterIds[0]").exists());
    }

    @Test
    void getStorySummaries_returnsEmpty_whenNoStories() throws Exception {
        sessionRepository.deleteAll();
        storyConfigurationCharacterRepository.deleteAll();
        storyConfigurationRepository.deleteAll();
        characterRepository.deleteAll();
        storyRepository.deleteAll();
        mockMvc.perform(get("/api/stories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
}
