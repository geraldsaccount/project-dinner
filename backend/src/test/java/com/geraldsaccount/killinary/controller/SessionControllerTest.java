package com.geraldsaccount.killinary.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.KillinaryApplication;
import com.geraldsaccount.killinary.TestDatabaseResetUtil;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.CreateSessionDto;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedSessionDto;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.SessionRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private StoryConfigurationRepository configRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestDatabaseResetUtil databaseResetUtil;

    private User host;
    private User participant;
    private Session session;
    private Story story;
    private Story newStory;
    private StoryConfiguration config;

    @BeforeEach
    @Transactional
    void setUp() {
        databaseResetUtil.resetDatabase();

        host = userRepository.save(User.builder()
                .oauthId("hostuser")
                .name("Sherlock")
                .email("sherlock@holmes.com")
                .build());
        participant = userRepository.save(User.builder()
                .oauthId("testuser")
                .name("Mycroft")
                .email("mycroft@holmes.com")
                .build());
        userRepository.save(User.builder()
                .oauthId("notparticipating")
                .name("Lestrade")
                .email("lestrade@holmes.com")
                .build());

        story = storyRepository.save(Story.builder()
                .title("Murder Mystery")
                .build());

        newStory = storyRepository.save(Story.builder()
                .title("Other Mystery")
                .build());
        config = configRepository.save(StoryConfiguration.builder()
                .story(newStory)
                .build());
        newStory.setConfigurations(Set.of(config));
        storyRepository.save(newStory);

        session = Session.builder()
                .host(host)
                .story(story)
                .status(SessionStatus.CONCLUDED)
                .startedAt(LocalDateTime.of(2025, 6, 2, 18, 0))
                .build();

        session = sessionRepository.save(session);

        session.setParticipants(Set.of(host, participant));

        Character character = characterRepository.save(Character.builder()
                .name("Watson")
                .gender(Gender.MALE)
                .story(story)
                .build());

        CharacterAssignment assignment = CharacterAssignment.builder()
                .session(session)
                .user(participant)
                .character(character)
                .build();

        Character hostCharacter = characterRepository.save(Character.builder()
                .name("Holmes")
                .gender(Gender.MALE)
                .story(story)
                .build());

        CharacterAssignment hostAssignment = CharacterAssignment.builder()
                .session(session)
                .user(host)
                .character(hostCharacter)
                .build();

        session.setCharacterAssignments(Set.of(assignment, hostAssignment));
        sessionRepository.save(session);
        userRepository.saveAll(List.of(host.withSessions(Set.of(session)), participant.withSessions(Set.of(session))));
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void getSessionsForUser_returnsSessionSummaries_forAuthenticatedUser() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/sessions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("Sherlock");
                    assertThat(json).contains("Murder Mystery");
                    assertThat(json).contains("Watson");
                    assertThat(json).contains("2025-06-02T18:00:00");
                });
    }

    @Test
    void getSessionsForUser_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/sessions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createSession_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/sessions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "invaliduser", roles = { "USER" })
    void createSession_returnsBadRequest_whenNoContent() throws Exception {
        mockMvc.perform(post("/api/sessions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "invaliduser", roles = { "USER" })
    void createSession_returnsNotFound_whenUserNotFound() throws Exception {
        CreateSessionDto dto = CreateSessionDto.builder()
                .build();
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createSession_returnsBadRequest_whenUserAlreadyPlayed() throws JsonProcessingException, Exception {
        CreateSessionDto dto = CreateSessionDto.builder()
                .storyId(session.getStory().getId())
                .build();
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createSession_returnsNotFound_whenStoryNotFound() throws JsonProcessingException, Exception {
        CreateSessionDto dto = CreateSessionDto.builder()
                .storyId(UUID.randomUUID())
                .build();
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createSession_returnsNotFound_whenStoryConfigNotFound() throws JsonProcessingException, Exception {
        CreateSessionDto dto = CreateSessionDto.builder()
                .storyId(newStory.getId())
                .storyConfigurationId(UUID.randomUUID())
                .build();
        String response = mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createSession_returnsSessionCreatedDTO_whenValidData()
            throws JsonProcessingException, UnsupportedEncodingException, Exception {
        CreateSessionDto dto = CreateSessionDto.builder()
                .storyId(newStory.getId())
                .storyConfigurationId(config.getId())
                .build();
        String response = mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        CreatedSessionDto newSession = objectMapper.readValue(response, CreatedSessionDto.class);

        assertThat(newSession.sessionId()).isNotNull();
    }

    @Test
    void getSessionView_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/sessions/" + session.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void getSessionView_returnsSessionView_forAuthenticatedParticipant() throws Exception {
        mockMvc.perform(get("/api/sessions/" + session.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("Murder Mystery");
                    assertThat(json).contains("Sherlock");
                    assertThat(json).contains("Watson");
                });
    }

    @Test
    @WithMockUser(username = "hostuser", roles = { "USER" })
    void getSessionView_returnsSessionView_forHost() throws Exception {
        mockMvc.perform(get("/api/sessions/" + session.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("Murder Mystery");
                    assertThat(json).contains("Sherlock");
                });
    }

    @Test
    @WithMockUser(username = "unknownuser", roles = { "USER" })
    void getSessionView_returnsForbidden_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/sessions/" + session.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "notparticipating", roles = { "USER" })
    void getSessionView_returnsForbidden_whenUserNotParticipantOrHost() throws Exception {
        mockMvc.perform(get("/api/sessions/" + session.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void getSessionView_returnsNotFound_whenSessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/sessions/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}