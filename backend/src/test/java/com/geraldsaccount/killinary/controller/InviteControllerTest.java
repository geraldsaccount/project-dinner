package com.geraldsaccount.killinary.controller;

import java.util.List;
import java.util.Map;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.geraldsaccount.killinary.model.dto.output.other.InvitationViewDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.CharacterStageInfo;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Stage;
import com.geraldsaccount.killinary.model.mystery.StageEvent;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.model.mystery.id.CharacterStageInfoId;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.DinnerRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.PlayerConfigRepository;
import com.geraldsaccount.killinary.repository.StageRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InviteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MysteryRepository mysteryRepository;
    @Autowired
    private DinnerRepository dinnerRepository;
    @Autowired
    private PlayerConfigRepository configRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private CharacterAssignmentRepository assignmentRepository;

    @Autowired
    private TestDatabaseResetUtil databaseResetUtil;

    private final String inviteCode = "INVITE123";
    private PlayerConfig config;
    private Character character;

    @BeforeEach
    @Transactional
    void setUp() {
        Mystery mystery = saveNewMystery();

        User user = userRepository.save(User.builder()
                .oauthId("testuser")
                .name("Test User")
                .email("test@user.com")
                .build());
        User host = userRepository.save(User.builder()
                .oauthId("hostuser")
                .name("Host User")
                .email("host@user.com")
                .build());
        Dinner dinner = dinnerRepository.save(Dinner.builder()
                .host(host)
                .mystery(mystery)
                .status(DinnerStatus.CREATED)
                .config(config)
                .build());
        CharacterAssignment assignment = assignmentRepository.save(CharacterAssignment.builder()
                .dinner(dinner)
                .character(character)
                .user(user)
                .code(inviteCode)
                .build());
        dinner.setCharacterAssignments(Set.of(assignment));
        dinnerRepository.save(dinner);
    }

    Mystery saveNewMystery() {
        character = characterRepository.save(Character.builder()
                .name("Alice")
                .build());
        Character character2 = characterRepository.save(Character.builder()
                .name("Bob")
                .build());

        Map<UUID, String> relationships1 = Map.of(character.getId(), "Friend");
        Map<UUID, String> relationships2 = Map.of(character2.getId(), "Friend");

        Stage stage = stageRepository.save(Stage.builder()
                .title("Stage 1")
                .build());

        StageEvent event1 = StageEvent.builder().title("Something").order(0).build();
        StageEvent event2 = StageEvent.builder().title("Something").order(0).build();

        CharacterStageInfo stageInfo1 = CharacterStageInfo.builder()
                .id(new CharacterStageInfoId(stage.getId(), character.getId()))
                .order(0)
                .events(List.of(event1))
                .build();
        CharacterStageInfo stageInfo2 = CharacterStageInfo.builder()
                .id(new CharacterStageInfoId(stage.getId(), character2.getId()))
                .order(0)
                .events(List.of(event2))
                .build();
        character = characterRepository
                .save(character.withRelationships(relationships1).withStageInfo(List.of(stageInfo1)));
        character2 = characterRepository
                .save(character2.withRelationships(relationships2).withStageInfo(List.of(stageInfo2)));

        config = PlayerConfig.builder()
                .playerCount(2)
                .characters(Set.of(character, character2))
                .build();

        return mysteryRepository.save(Mystery.builder()
                .story(Story.builder().title("Title").build())
                .characters(List.of(character, character2))
                .stages(List.of(stage))
                .setups(List.of(config))
                .build());
    }

    @Test
    void getInvitation_returnsOk_andInvitationDTO_whenInviteExists() throws Exception {
        mockMvc.perform(get("/api/invite/" + inviteCode)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    InvitationViewDto dto = objectMapper.readValue(json,
                            InvitationViewDto.class);
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
