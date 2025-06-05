package com.geraldsaccount.killinary.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
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
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.SessionParticipant;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.SessionCreationDTO;
import com.geraldsaccount.killinary.model.dto.output.NewSessionDTO;
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

    private User host;
    private User participant;
    private Session session;
    private Story story;
    private Story newStory;
    private StoryConfiguration config;

    @BeforeEach
    @Transactional
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        host = userRepository.save(User.builder()
                .oauthId("hostuser")
                .firstName("Sherlock")
                .email("sherlock@holmes.com")
                .build());
        participant = userRepository.save(User.builder()
                .oauthId("testuser")
                .firstName("Mycroft")
                .email("mycroft@holmes.com")
                .build());

        story = storyRepository.save(Story.builder()
                .title("Murder Mystery")
                .build());

        newStory = storyRepository.save(Story.builder()
                .title("Other Mystery")
                .build());
        config = configRepository.save(StoryConfiguration.builder()
                .story(newStory)
                .playerCount(3)
                .configurationName("3 Player Config")
                .build());
        newStory.setConfigurations(Set.of(config));
        storyRepository.save(newStory);

        session = Session.builder()
                .host(host)
                .story(story)
                .code("221b")
                .status(SessionStatus.CONCLUDED)
                .startedAt(LocalDateTime.of(2025, 6, 2, 18, 0))
                .build();

        session = sessionRepository.save(session);

        SessionParticipant sessionParticipant = new SessionParticipant(session, participant);
        session.setParticipants(Set.of(sessionParticipant));

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

        session.setCharacterAssignments(Set.of(assignment));
        sessionRepository.save(session);
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
        SessionCreationDTO dto = SessionCreationDTO.builder()
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
        SessionCreationDTO dto = SessionCreationDTO.builder()
                .storyId(session.getStory().getId())
                .build();
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createSession_returnsNotFound_whenStoryNotFound() throws JsonProcessingException, Exception {
        SessionCreationDTO dto = SessionCreationDTO.builder()
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
        SessionCreationDTO dto = SessionCreationDTO.builder()
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
        SessionCreationDTO dto = SessionCreationDTO.builder()
                .storyId(newStory.getId())
                .storyConfigurationId(config.getId())
                .build();
        String response = mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        NewSessionDTO newSession = objectMapper.readValue(response, NewSessionDTO.class);

        assertThat(newSession.sessionId()).isNotNull();
    }
}