package com.geraldsaccount.killinary.controller;

import java.time.LocalDateTime;
import java.util.Set;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.KillinaryApplication;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionCharacterAssignment;
import com.geraldsaccount.killinary.model.SessionParticipant;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.SessionRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
class SessionControlerTest {

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
    private ObjectMapper objectMapper;

    private User host;
    private User participant;
    private Session session;

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

        Story story = storyRepository.save(Story.builder()
                .title("Murder Mystery")
                .build());

        session = Session.builder()
                .host(host)
                .story(story)
                .sessionCode("221b")
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

        SessionCharacterAssignment assignment = SessionCharacterAssignment.builder()
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
}