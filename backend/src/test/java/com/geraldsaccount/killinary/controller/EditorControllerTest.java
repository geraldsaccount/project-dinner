package com.geraldsaccount.killinary.controller;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import com.geraldsaccount.killinary.model.mystery.Gender;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.PlayerConfigRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EditorControllerTest {
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
    @WithMockUser(username = "testuser", roles = { "USER" })
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
                .avatarImage("avatar1.png") // or null
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
                .avatarImage("avatar2.png") // or null
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

        MockMultipartFile mysteryJsonPart = new MockMultipartFile(
                "mystery", // This MUST match the @RequestPart name in your controller
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(mysteryDto));

        // 2. Create mock file parts
        MockMultipartFile bannerImageFile = new MockMultipartFile(
                "bannerImageFile", // This MUST match the key used on the frontend/service
                "banner.png",
                MediaType.IMAGE_PNG_VALUE,
                "banner-image-bytes".getBytes());

        // The key for character avatars MUST match the character's ID
        MockMultipartFile avatarFile1 = new MockMultipartFile(
                charDto1.id(), // Key is "C1"
                "avatar1.png",
                MediaType.IMAGE_PNG_VALUE,
                "avatar-1-bytes".getBytes());

        MockMultipartFile avatarFile2 = new MockMultipartFile(
                charDto2.id(), // Key is "C2"
                "avatar2.png",
                MediaType.IMAGE_PNG_VALUE,
                "avatar-2-bytes".getBytes());

        // 3. Perform the multipart request
        mockMvc.perform(multipart("/api/editor")
                .file(mysteryJsonPart)
                .file(bannerImageFile)
                .file(avatarFile1)
                .file(avatarFile2))
                .andExpect(status().isOk());
    }
}
