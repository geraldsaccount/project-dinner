package com.geraldsaccount.killinary.controller;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.KillinaryApplication;
import com.geraldsaccount.killinary.TestDatabaseResetUtil;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dinner.DinnerStatus;
import com.geraldsaccount.killinary.model.dto.input.CreateDinnerDto;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedDinnerDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Gender;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.DinnerRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.PlayerConfigRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DinnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DinnerRepository dinnerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private PlayerConfigRepository configRepository;

    @Autowired
    private MysteryRepository mysteryRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestDatabaseResetUtil databaseResetUtil;

    private User host;
    private User participant;
    private Dinner dinner;
    private PlayerConfig config;
    private Mystery newMystery;

    @BeforeEach
    @Transactional
    void setUp() {
        buildUsers();

        Character character = characterRepository.save(Character.builder()
                .name("Watson")
                .gender(Gender.MALE)
                .build());

        Character hostCharacter = characterRepository.save(Character.builder()
                .name("Holmes")
                .gender(Gender.MALE)
                .build());

        config = PlayerConfig.builder()
                .characters(Set.of(hostCharacter, character))
                .playerCount(2)
                .build();

        Mystery mystery = mysteryRepository.save(Mystery.builder()
                .story(Story.builder().title("Murder Mystery")
                        .build())
                .build());

        newMystery = mysteryRepository.save(Mystery.builder()
                .story(Story.builder().title("Murder Mystery")
                        .build())
                .setups(List.of(config))
                .build());

        dinner = dinnerRepository.save(Dinner.builder()
                .host(host)
                .mystery(mystery)
                .config(config)
                .status(DinnerStatus.CONCLUDED)
                .date(LocalDateTime.of(2025, 6, 2, 18, 0))
                .build());

        CharacterAssignment assignment = CharacterAssignment.builder()
                .dinner(dinner)
                .user(participant)
                .character(character)
                .build();
        CharacterAssignment hostAssignment = CharacterAssignment.builder()
                .dinner(dinner)
                .user(host)
                .character(hostCharacter)
                .build();
        dinner.setParticipants(Set.of(host, participant));
        dinner.setCharacterAssignments(Set.of(assignment, hostAssignment));
        dinnerRepository.save(dinner);
        userRepository.saveAll(List.of(host.withDinners(Set.of(dinner)), participant.withDinners(Set.of(dinner))));
    }

    private void buildUsers() {
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
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void getDinnersForUser_returnsDinnerSummaries_forAuthenticatedUser() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/dinners")
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
    void getDinnersForUser_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/dinners")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createDinner_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/dinners")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "invaliduser", roles = { "USER" })
    void createDinner_returnsBadRequest_whenNoContent() throws Exception {
        mockMvc.perform(post("/api/dinners")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "invaliduser", roles = { "USER" })
    void createDinner_returnsNotFound_whenUserNotFound() throws Exception {
        CreateDinnerDto dto = CreateDinnerDto.builder()
                .build();
        mockMvc.perform(post("/api/dinners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createDinner_returnsBadRequest_whenUserAlreadyPlayed() throws Exception {
        CreateDinnerDto dto = CreateDinnerDto.builder()
                .storyId(dinner.getMystery().getId())
                .build();
        mockMvc.perform(post("/api/dinners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createDinner_returnsNotFound_whenStoryNotFound() throws Exception {
        CreateDinnerDto dto = CreateDinnerDto.builder()
                .storyId(UUID.randomUUID())
                .build();
        mockMvc.perform(post("/api/dinners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createDinner_returnsNotFound_whenStoryConfigNotFound() throws Exception {
        CreateDinnerDto dto = CreateDinnerDto.builder()
                .storyId(newMystery.getId())
                .storyConfigurationId(UUID.randomUUID())
                .build();
        String response = mockMvc.perform(post("/api/dinners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void createDinner_returnsDinnerCreatedDTO_whenValidData() throws Exception {
        CreateDinnerDto dto = CreateDinnerDto.builder()
                .storyId(newMystery.getId())
                .storyConfigurationId(config.getId())
                .build();
        String response = mockMvc.perform(post("/api/dinners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        CreatedDinnerDto newDinner = objectMapper.readValue(response,
                CreatedDinnerDto.class);

        assertThat(newDinner.dinnerId()).isNotNull();
    }

    @Test
    void getDinnerView_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/dinners/" + dinner.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void getDinnerView_returnsDinnerView_forAuthenticatedParticipant() throws Exception {
        mockMvc.perform(get("/api/dinners/" + dinner.getId())
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
    void getDinnerView_returnsDinnerView_forHost() throws Exception {
        mockMvc.perform(get("/api/dinners/" + dinner.getId())
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
    void getDinnerView_returnsForbidden_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/dinners/" + dinner.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "notparticipating", roles = { "USER" })
    void getDinnerView_returnsForbidden_whenUserNotParticipantOrHost() throws Exception {
        mockMvc.perform(get("/api/dinners/" + dinner.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void getDinnerView_returnsNotFound_whenDinnerDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/dinners/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}