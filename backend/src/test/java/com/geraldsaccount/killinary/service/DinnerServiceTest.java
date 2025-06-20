package com.geraldsaccount.killinary.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.MysteryNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.DinnerMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dto.input.CreateDinnerDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerView;
import com.geraldsaccount.killinary.model.dto.output.dinner.GuestDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.HostDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.repository.DinnerRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class DinnerServiceTest {

    @Mock
    private DinnerRepository dinnerRepository;

    @Mock
    private UserService userService;

    @Mock
    private MysteryService mysteryService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private DinnerMapper dinnerMapper;

    @Mock
    private CharacterAssignmentCodeService assignmentCodeService;

    @Mock
    private CharacterMapper characterMapper;

    @InjectMocks
    private DinnerService dinnerService;

    private User user;
    private User host;
    private Dinner dinner;
    private Mystery mystery;
    private Character character;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void createDummyDinner() {
        host = User.builder()
                .oauthId("UH")
                .name("Host")
                .build();
        user = User.builder()
                .oauthId("U1")
                .build();
        byte[] bannerBytes = "dummy-banner-data".getBytes();
        Story story = Story.builder()
                .title("Some Story")
                .bannerImage(bannerBytes)
                .build();
        mystery = Mystery.builder()
                .story(story)
                .build();
        character = Character.builder()
                .name("Sir Archibald")
                .build();
        CharacterAssignment assignment = CharacterAssignment.builder()
                .character(character)
                .user(user)
                .build();

        dinner = Dinner.builder()
                .id(UUID.randomUUID())
                .host(host)
                .mystery(mystery)
                .characterAssignments(Set.of(assignment))
                .date(LocalDateTime.of(2025, 6, 2, 18, 0))
                .build();
    }

    @Test
    void getDinnerSummariesFrom_returnsEmptySet_whenNoDinners() {
        when(dinnerRepository.findAllByUserId("U1")).thenReturn(Collections.emptyList());

        Set<DinnerSummaryDto> result = dinnerService.getDinnerSummariesFrom("U1");

        assertThat(result).isEmpty();
    }

    @Test
    void getDinnerSummariesFrom_returnsDinnerSummary_whenDinnerExistsAndUserAssigned() {
        createDummyDinner();
        when(dinnerRepository.findAllByUserId(user.getOauthId())).thenReturn(List.of(dinner));

        UserDto hostDto = new UserDto(user.getId(), user.getName(),
                user.getAvatarUrl());
        when(userMapper.asDTO(host)).thenReturn(hostDto);

        Set<DinnerSummaryDto> result = dinnerService.getDinnerSummariesFrom(user.getOauthId());

        assertThat(result).hasSize(1);
        DinnerSummaryDto dto = result.iterator().next();

        String expectedBannerData = Base64.getEncoder().encodeToString(mystery.getStory().getBannerImage());

        DinnerSummaryDto expected = new DinnerSummaryDto(
                dinner.getId(),
                dinner.getDate(),
                userMapper.asDTO(host),
                mystery.getStory().getTitle(),
                expectedBannerData,
                character.getName());

        assertThat(dto).isEqualTo(expected);
    }

    @Test
    void getDinnerSummariesFrom_assignedCharacterNameIsNull_whenUserNotAssigned() {
        createDummyDinner();
        dinner.setCharacterAssignments(Collections.emptySet());
        when(dinnerRepository.findAllByUserId(user.getOauthId())).thenReturn(List.of(dinner));

        UserDto hostDto = new UserDto(user.getId(), user.getName(),
                user.getAvatarUrl());
        when(userMapper.asDTO(host)).thenReturn(hostDto);

        Set<DinnerSummaryDto> result = dinnerService.getDinnerSummariesFrom(user.getOauthId());

        assertThat(result).hasSize(1);
        DinnerSummaryDto dto = result.iterator().next();
        String expectedBannerData = Base64.getEncoder().encodeToString(mystery.getStory().getBannerImage());

        DinnerSummaryDto expected = new DinnerSummaryDto(
                dinner.getId(),
                dinner.getDate(),
                userMapper.asDTO(host),
                mystery.getStory().getTitle(),
                expectedBannerData,
                "TBD");

        assertThat(dto)
                .isEqualTo(expected);
    }

    @Test
    void createDinner_throwsUserNotFound_withInvalidOathId() throws Exception {
        String invalidId = "invalid-id";
        when(userService.getUserOrThrow(invalidId)).thenThrow(new UserNotFoundException("Could not find host."));
        CreateDinnerDto creationDTO = mock(CreateDinnerDto.class);

        assertThatThrownBy(() -> {
            dinnerService.createDinner(invalidId, creationDTO);
        }).isInstanceOf(UserNotFoundException.class)
                .hasMessage("Could not find host.");
    }

    @Test
    void createDinner_throwsNotAllowed_whenHostAlreadyKnowsStory() throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID participatedId = UUID.randomUUID();
        User testHost = mock(User.class);
        CreateDinnerDto creationDTO = CreateDinnerDto.builder()
                .storyId(participatedId)
                .build();

        when(userService.getUserOrThrow(validOauthId)).thenReturn(testHost);
        doThrow(new AccessDeniedException("User cannot play a Story multiple times"))
                .when(userService).validateHasNotPlayedStory(testHost, participatedId);

        assertThatThrownBy(() -> dinnerService.createDinner(validOauthId,
                creationDTO))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("User cannot play a Story multiple times");
    }

    @Test
    void createDinner_throwsStoryNotFound_withInvalidStoryId() throws Exception {
        String validOauthId = "valid-oauth-id";
        host = mock(User.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getDinners()).thenReturn(Set.of());
        when(mysteryService.getMysteryOrThrow(any())).thenThrow(new MysteryNotFoundException("Could not find Story."));
        CreateDinnerDto creationDTO = CreateDinnerDto.builder()
                .storyId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> {
            dinnerService.createDinner(validOauthId, creationDTO);
        }).isInstanceOf(MysteryNotFoundException.class)
                .hasMessage("Could not find Story.");
    }

    @Test
    void createDinner_throwsStoryConfigurationNotFound_withInvalidConfigurationId()
            throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        host = mock(User.class);
        mystery = mock(Mystery.class);
        PlayerConfig config = mock(PlayerConfig.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getDinners()).thenReturn(Set.of());
        when(mysteryService.getMysteryOrThrow(storyId)).thenReturn(mystery);
        when(mystery.getSetups()).thenReturn(List.of(config));
        when(config.getId()).thenReturn(UUID.randomUUID());

        CreateDinnerDto creationDTO = CreateDinnerDto.builder()
                .storyId(storyId)
                .storyConfigurationId(UUID.randomUUID())
                .build();
        assertThatThrownBy(() -> {
            dinnerService.createDinner(validOauthId, creationDTO);
        }).isInstanceOf(StoryConfigurationNotFoundException.class)
                .hasMessage("Could not find Story Configuration");
    }

    @Test
    void createDinner_throwsStoryConfigurationNotFound_withStoryContainingNoConfigs()
            throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        host = mock(User.class);
        mystery = mock(Mystery.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getDinners()).thenReturn(Set.of());
        when(mysteryService.getMysteryOrThrow(storyId)).thenReturn(mystery);
        when(mystery.getSetups()).thenReturn(List.of());

        CreateDinnerDto creationDTO = CreateDinnerDto.builder()
                .storyId(storyId)
                .storyConfigurationId(UUID.randomUUID())
                .build();
        assertThatThrownBy(() -> {
            dinnerService.createDinner(validOauthId, creationDTO);
        }).isInstanceOf(StoryConfigurationNotFoundException.class)
                .hasMessage("Could not find Story Configuration");
    }

    @Test
    void createDinner_throwsRuntime_whenTooManyCodeGenerationAttempts() throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        UUID configId = UUID.randomUUID();
        host = mock(User.class);
        mystery = mock(Mystery.class);
        PlayerConfig config = mock(PlayerConfig.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getDinners()).thenReturn(Set.of());
        when(mysteryService.getMysteryOrThrow(storyId)).thenReturn(mystery);
        when(mystery.getSetups()).thenReturn(List.of(config));
        when(config.getId()).thenReturn(configId);

        when(dinnerRepository.save(any())).thenThrow(new RuntimeException("Duplicate code"));

        CreateDinnerDto creationDTO = CreateDinnerDto.builder()
                .storyId(storyId)
                .storyConfigurationId(configId)
                .build();

        assertThatThrownBy(() -> dinnerService.createDinner(validOauthId,
                creationDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Duplicate code");
    }

    @Test
    void createDinner_returnsDinnerDTO_withValidInputs() throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        UUID configId = UUID.randomUUID();
        UUID dinnerId = UUID.randomUUID();
        host = mock(User.class);
        mystery = mock(Mystery.class);
        dinner = mock(Dinner.class);
        PlayerConfig config = mock(PlayerConfig.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getDinners()).thenReturn(Set.of());
        when(mysteryService.getMysteryOrThrow(storyId)).thenReturn(mystery);
        when(mystery.getSetups()).thenReturn(List.of(config));
        when(config.getId()).thenReturn(configId);
        when(dinnerRepository.save(any())).thenReturn(dinner);
        when(dinner.getId()).thenReturn(dinnerId);
        when(dinner.getConfig()).thenReturn(config);
        when(dinner.getMystery()).thenReturn(mystery);

        CreateDinnerDto creationDTO = CreateDinnerDto.builder()
                .storyId(storyId)
                .storyConfigurationId(configId)
                .build();

        assertThat(dinnerService.createDinner(validOauthId,
                creationDTO).dinnerId())
                .isEqualTo(dinnerId);

        verify(dinnerRepository, times(2)).save(any());
    }

    @Test
    void getDinnerView_throwsUserNotFound_whenUserDoesNotExist() throws Exception {
        UUID dinnerId = UUID.randomUUID();
        when(userService.getUserOrThrow("bad-oauth")).thenThrow(new UserNotFoundException("User not found"));

        assertThatThrownBy(() -> dinnerService.getDinnerView("bad-oauth", dinnerId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void getDinnerView_throwsDinnerNotFound_whenDinnerDoesNotExist() throws Exception {
        String oauthId = "user-oauth";
        UUID dinnerId = UUID.randomUUID();
        user = User.builder().oauthId(oauthId).build();
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(dinnerRepository.findById(dinnerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dinnerService.getDinnerView(oauthId, dinnerId))
                .isInstanceOf(com.geraldsaccount.killinary.exceptions.DinnerNotFoundException.class)
                .hasMessage("Could not find dinner");
    }

    @Test
    void getDinnerView_throwsAccessDenied_whenUserNotInDinner() throws Exception {
        String oauthId = "user-oauth";
        UUID dinnerId = UUID.randomUUID();
        host = User.builder().oauthId("UH").name("Host").build();
        user = User.builder().oauthId(oauthId).build();
        dinner = Dinner.builder().id(dinnerId).host(host).participants(Set.of()).build();
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(dinnerRepository.findById(dinnerId)).thenReturn(Optional.of(dinner));

        assertThatThrownBy(() -> dinnerService.getDinnerView(oauthId, dinnerId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Cannot access dinner data.");
    }

    @Test
    void getDinnerView_returnsGuestView_whenUserIsGuest() throws Exception {
        String oauthId = "guest-oauth";
        UUID dinnerId = UUID.randomUUID();
        user = User.builder().id(UUID.randomUUID()).oauthId(oauthId).build();
        host = User.builder().id(UUID.randomUUID()).oauthId("host-oauth").build();
        CharacterAssignment assignment = CharacterAssignment.builder().user(user).build();
        dinner = Dinner.builder().id(dinnerId).host(host).characterAssignments(Set.of(assignment)).build();

        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(dinnerRepository.findById(dinnerId)).thenReturn(Optional.of(dinner));

        DinnerView dinnerView = dinnerService.getDinnerView(oauthId, dinnerId);

        assertThat(dinnerView).isInstanceOf(GuestDinnerViewDto.class);
        verify(dinnerMapper).getPreDinnerInfo(dinner);
        verify(dinnerMapper).getPrivateInfoForUser(user, dinner);
        verify(dinnerMapper).getConclusion(dinner);
        verify(dinnerMapper, times(0)).getHostInfo(any());
    }

    @Test
    void getDinnerView_returnsHostView_whenUserIsHost() throws Exception {
        String oauthId = "host-oauth";
        UUID dinnerId = UUID.randomUUID();
        host = User.builder().id(UUID.randomUUID()).oauthId(oauthId).build();
        user = host;
        CharacterAssignment assignment = CharacterAssignment.builder().user(user).build();
        dinner = Dinner.builder().id(dinnerId).host(host).characterAssignments(Set.of(assignment)).build();

        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(dinnerRepository.findById(dinnerId)).thenReturn(Optional.of(dinner));

        DinnerView dinnerView = dinnerService.getDinnerView(oauthId, dinnerId);

        assertThat(dinnerView).isInstanceOf(HostDinnerViewDto.class);
        verify(dinnerMapper).getPreDinnerInfo(dinner);
        verify(dinnerMapper).getPrivateInfoForUser(user, dinner);
        verify(dinnerMapper).getConclusion(dinner);
        verify(dinnerMapper).getHostInfo(dinner);
    }
}