package com.geraldsaccount.killinary.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.KillinaryApplication;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.output.other.InvitationViewDto;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.SessionRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
class InviteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CharacterAssignmentRepository assignmentRepository;

    private User user;
    private User host;
    private Story story;
    private Session session;
    private Character character;
    private CharacterAssignment assignment;
    private String inviteCode;

    @BeforeEach
    @Transactional
    void setUp() {
        assignmentRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        storyRepository.deleteAll();
        characterRepository.deleteAll();

        user = userRepository.save(User.builder()
                .oauthId("testuser")
                .name("Test User")
                .email("test@user.com")
                .build());
        host = userRepository.save(User.builder()
                .oauthId("hostuser")
                .name("Host User")
                .email("host@user.com")
                .build());
        story = storyRepository.save(Story.builder()
                .title("Test Story")
                .build());
        session = sessionRepository.save(Session.builder()
                .host(host)
                .story(story)
                .status(SessionStatus.CREATED)
                .build());
        character = characterRepository.save(Character.builder()
                .name("Detective")
                .story(story)
                .gender(Gender.OTHER)
                .build());
        inviteCode = "INVITE123";
        assignment = assignmentRepository.save(CharacterAssignment.builder()
                .session(session)
                .character(character)
                .user(user)
                .code(inviteCode)
                .build());
        session.setCharacterAssignments(java.util.Set.of(assignment));
        sessionRepository.save(session);
    }

    @Test
    void getInvitation_returnsOk_andInvitationDTO_whenInviteExists() throws Exception {
        mockMvc.perform(get("/api/invite/" + inviteCode)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    InvitationViewDto dto = objectMapper.readValue(json, InvitationViewDto.class);
                    assertThat(dto).isNotNull();
                    assertThat(dto.inviteCode()).isEqualTo(inviteCode);
                });
    }

    @Test
    void getInvitation_returnsNotFound_whenInviteDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/invite/DOESNOTEXIST")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void acceptInvitation_succeeds_whenValid() throws Exception {
        // Assign the user to the assignment
        mockMvc.perform(put("/api/invite/" + inviteCode)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void acceptInvitation_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(put("/api/invite/" + inviteCode)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void acceptInvitation_returnsNotFound_whenInviteDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/invite/DOESNOTEXIST")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
