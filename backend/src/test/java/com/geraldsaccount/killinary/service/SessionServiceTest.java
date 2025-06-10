package com.geraldsaccount.killinary.service;

import java.time.LocalDateTime;
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
import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionParticipant;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.CreateSessionDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.CharacterAssignmentDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerParticipantDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerView;
import com.geraldsaccount.killinary.model.dto.output.dinner.GuestDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.HostDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;
import com.geraldsaccount.killinary.repository.SessionRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserService userService;

    @Mock
    private StoryService storyService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CharacterAssignmentCodeService assignmentCodeService;

    @Mock
    private CharacterMapper characterMapper;

    @InjectMocks
    private SessionService sessionService;

    private User user;
    private User host;
    private Session session;
    private Story story;
    private Character character;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSessionSummariesFrom_returnsEmptySet_whenNoSessions() {
        when(sessionRepository.findAllByUserId("U1")).thenReturn(Collections.emptyList());

        Set<DinnerSummaryDto> result = sessionService.getSessionSummariesFrom("U1");

        assertThat(result).isEmpty();
    }

    @Test
    void getSessionSummariesFrom_returnsSessionSummary_whenSessionExistsAndUserAssigned() {
        createDummySession();
        when(sessionRepository.findAllByUserId(user.getOauthId())).thenReturn(List.of(session));

        UserDto hostDto = new UserDto(user.getId(), user.getName(), user.getAvatarUrl());
        when(userMapper.asDTO(host)).thenReturn(hostDto);

        Set<DinnerSummaryDto> result = sessionService.getSessionSummariesFrom(user.getOauthId());

        assertThat(result).hasSize(1);
        DinnerSummaryDto dto = result.iterator().next();

        DinnerSummaryDto expected = new DinnerSummaryDto(
                session.getId(),
                session.getStartedAt(),
                userMapper.asDTO(host),
                story.getTitle(),
                story.getBannerUrl(),
                character.getName());

        assertThat(dto)
                .isEqualTo(expected);
    }

    @Test
    void getSessionSummariesFrom_assignedCharacterNameIsNull_whenUserNotAssigned() {
        createDummySession();
        session.setCharacterAssignments(Collections.emptySet());
        when(sessionRepository.findAllByUserId(user.getOauthId())).thenReturn(List.of(session));

        UserDto hostDto = new UserDto(user.getId(), user.getName(), user.getAvatarUrl());
        when(userMapper.asDTO(host)).thenReturn(hostDto);

        Set<DinnerSummaryDto> result = sessionService.getSessionSummariesFrom(user.getOauthId());

        assertThat(result).hasSize(1);
        DinnerSummaryDto dto = result.iterator().next();

        DinnerSummaryDto expected = new DinnerSummaryDto(
                session.getId(),
                session.getStartedAt(),
                userMapper.asDTO(host),
                story.getTitle(),
                story.getBannerUrl(),
                null);

        assertThat(dto)
                .isEqualTo(expected);
    }

    @Test
    void createSession_throwsUserNotFound_withInvalidOathId() throws Exception {
        String invalidId = "invalid-id";
        when(userService.getUserOrThrow(invalidId)).thenThrow(new UserNotFoundException("Could not find host."));
        CreateSessionDto creationDTO = mock(CreateSessionDto.class);

        assertThatThrownBy(() -> {
            sessionService.createSession(invalidId, creationDTO);
        }).isInstanceOf(UserNotFoundException.class)
                .hasMessage("Could not find host.");
    }

    @Test
    void createSession_throwsNotAllowed_whenHostAlreadyKnowsStory() throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID participatedId = UUID.randomUUID();
        User testHost = mock(User.class);
        CreateSessionDto creationDTO = CreateSessionDto.builder()
                .storyId(participatedId)
                .build();

        when(userService.getUserOrThrow(validOauthId)).thenReturn(testHost);
        doThrow(new AccessDeniedException("User cannot play a Story multiple times"))
                .when(userService).validateHasNotPlayedStory(testHost, participatedId);

        assertThatThrownBy(() -> sessionService.createSession(validOauthId, creationDTO))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("User cannot play a Story multiple times");
    }

    @Test
    void createSession_throwsStoryNotFound_withInvalidStoryId() throws Exception {
        String validOauthId = "valid-oauth-id";
        host = mock(User.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyService.getStoryOrThrow(any())).thenThrow(new StoryNotFoundException("Could not find Story."));
        CreateSessionDto creationDTO = CreateSessionDto.builder()
                .storyId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> {
            sessionService.createSession(validOauthId, creationDTO);
        }).isInstanceOf(StoryNotFoundException.class)
                .hasMessage("Could not find Story.");
    }

    @Test
    void createSession_throwsStoryConfigurationNotFound_withInvalidConfigurationId()
            throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        host = mock(User.class);
        story = mock(Story.class);
        StoryConfiguration config = mock(StoryConfiguration.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyService.getStoryOrThrow(storyId)).thenReturn(story);
        when(story.getConfigurations()).thenReturn(Set.of(config));
        when(config.getId()).thenReturn(UUID.randomUUID());

        CreateSessionDto creationDTO = CreateSessionDto.builder()
                .storyId(storyId)
                .storyConfigurationId(UUID.randomUUID())
                .build();
        assertThatThrownBy(() -> {
            sessionService.createSession(validOauthId, creationDTO);
        }).isInstanceOf(StoryConfigurationNotFoundException.class)
                .hasMessage("Could not find Story Configuration");
    }

    @Test
    void createSession_throwsStoryConfigurationNotFound_withStoryContainingNoConfigs() throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        host = mock(User.class);
        story = mock(Story.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyService.getStoryOrThrow(storyId)).thenReturn(story);
        when(story.getConfigurations()).thenReturn(Set.of());

        CreateSessionDto creationDTO = CreateSessionDto.builder()
                .storyId(storyId)
                .storyConfigurationId(UUID.randomUUID())
                .build();
        assertThatThrownBy(() -> {
            sessionService.createSession(validOauthId, creationDTO);
        }).isInstanceOf(StoryConfigurationNotFoundException.class)
                .hasMessage("Could not find Story Configuration");
    }

    @Test
    void createSession_throwsRuntime_whenTooManyCodeGenerationAttempts() throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        UUID configId = UUID.randomUUID();
        host = mock(User.class);
        story = mock(Story.class);
        StoryConfiguration config = mock(StoryConfiguration.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyService.getStoryOrThrow(storyId)).thenReturn(story);
        when(story.getConfigurations()).thenReturn(Set.of(config));
        when(config.getId()).thenReturn(configId);

        when(sessionRepository.save(any())).thenThrow(new RuntimeException("Duplicate code"));

        CreateSessionDto creationDTO = CreateSessionDto.builder()
                .storyId(storyId)
                .storyConfigurationId(configId)
                .build();

        assertThatThrownBy(() -> sessionService.createSession(validOauthId, creationDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Duplicate code");
    }

    @Test
    void createSession_returnsSessionDTO_withValidInputs() throws Exception {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        UUID configId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        host = mock(User.class);
        story = mock(Story.class);
        session = mock(Session.class);
        StoryConfiguration config = mock(StoryConfiguration.class);

        when(userService.getUserOrThrow(validOauthId)).thenReturn(host);
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyService.getStoryOrThrow(storyId)).thenReturn(story);
        when(story.getConfigurations()).thenReturn(Set.of(config));
        when(config.getId()).thenReturn(configId);
        when(sessionRepository.save(any())).thenReturn(session);
        when(session.getId()).thenReturn(sessionId);
        when(session.getStoryConfiguration()).thenReturn(config);

        CreateSessionDto creationDTO = CreateSessionDto.builder()
                .storyId(storyId)
                .storyConfigurationId(configId)
                .build();

        assertThat(sessionService.createSession(validOauthId, creationDTO).sessionId())
                .isEqualTo(sessionId);

        verify(sessionRepository, times(2)).save(any());
    }

    private void createDummySession() {
        host = User.builder()
                .oauthId("UH")
                .name("Host")
                .build();
        user = User.builder()
                .oauthId("U1")
                .build();
        story = Story.builder()
                .title("Some Story")
                .build();
        character = Character.builder()
                .name("Sir Archibald")
                .build();
        CharacterAssignment assignment = CharacterAssignment.builder()
                .character(character)
                .user(user)
                .build();

        session = Session.builder()
                .id(UUID.randomUUID())
                .host(host)
                .story(story)
                .characterAssignments(Set.of(assignment))
                .startedAt(LocalDateTime.of(2025, 6, 2, 18, 0))
                .build();
    }

    @Test
    void getDinnerView_throwsUserNotFound_whenUserDoesNotExist() throws Exception {
        UUID dinnerId = UUID.randomUUID();
        when(userService.getUserOrThrow("bad-oauth")).thenThrow(new UserNotFoundException("User not found"));

        assertThatThrownBy(() -> sessionService.getDinnerView("bad-oauth", dinnerId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void getDinnerView_throwsSessionNotFound_whenSessionDoesNotExist() throws Exception {
        String oauthId = "user-oauth";
        UUID dinnerId = UUID.randomUUID();
        user = User.builder().oauthId(oauthId).build();
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(sessionRepository.findById(dinnerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.getDinnerView(oauthId, dinnerId))
                .isInstanceOf(com.geraldsaccount.killinary.exceptions.SessionNotFoundException.class)
                .hasMessage("Could not find session");
    }

    @Test
    void getDinnerView_throwsAccessDenied_whenUserNotInSession() throws Exception {
        String oauthId = "user-oauth";
        UUID dinnerId = UUID.randomUUID();
        user = User.builder().oauthId(oauthId).build();
        session = Session.builder().id(dinnerId).participants(Set.of()).build();
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(sessionRepository.findById(dinnerId)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> sessionService.getDinnerView(oauthId, dinnerId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Cannot access session data.");
    }

    @Test
    void getDinnerView_throwsCharacterAssignmentNotFound_whenUserHasNoAssignment() throws Exception {
        String oauthId = "user-oauth";
        UUID dinnerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        user = User.builder().id(userId).oauthId(oauthId).build();
        SessionParticipant participant = SessionParticipant.builder().user(user).build();
        session = Session.builder()
                .id(dinnerId)
                .participants(Set.of(participant))
                .host(User.builder().id(UUID.randomUUID()).build())
                .characterAssignments(Set.of())
                .build();
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(sessionRepository.findById(dinnerId)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> sessionService.getDinnerView(oauthId, dinnerId))
                .isInstanceOf(CharacterAssignmentNotFoundException.class)
                .hasMessage("User should have been assigned to a character.");
    }

    @Test
    void getDinnerView_returnsHostDinnerView_whenUserIsHost() throws Exception {
        String oauthId = "host-oauth";
        UUID dinnerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID characterId = UUID.randomUUID();
        UUID pendingCharacterId = UUID.randomUUID();

        User hostUser = User.builder().id(userId).oauthId(oauthId).name("Host").avatarUrl("avatar.png").build();
        story = Story.builder().title("Story Title").bannerUrl("banner.png").dinnerStoryBrief("brief").build();
        character = Character.builder().id(characterId).privateBriefing("private").build();
        Character pendingCharacter = Character.builder().id(pendingCharacterId).build();
        CharacterAssignment assigned = CharacterAssignment.builder().user(hostUser).character(character).code("CODE123")
                .build();
        CharacterAssignment pending = CharacterAssignment.builder().user(null).character(pendingCharacter)
                .code("INVITE456").build();
        SessionParticipant participant = SessionParticipant.builder().user(hostUser).build();
        session = Session.builder()
                .id(dinnerId)
                .host(hostUser)
                .story(story)
                .startedAt(LocalDateTime.now())
                .characterAssignments(Set.of(assigned, pending))
                .participants(Set.of(participant))
                .build();

        CharacterDetailDto characterDetailDto = mock(CharacterDetailDto.class);
        CharacterDetailDto pendingCharacterDetailDto = mock(CharacterDetailDto.class);
        when(userService.getUserOrThrow(oauthId)).thenReturn(hostUser);
        when(sessionRepository.findById(dinnerId)).thenReturn(Optional.of(session));
        when(characterMapper.asDetailDTO(character)).thenReturn(characterDetailDto);
        when(characterMapper.asDetailDTO(pendingCharacter)).thenReturn(pendingCharacterDetailDto);
        when(userMapper.asDTO(hostUser)).thenReturn(new UserDto(userId, "Host", "avatar.png"));

        DinnerView result = sessionService.getDinnerView(oauthId, dinnerId);

        assertThat(result).isInstanceOf(HostDinnerViewDto.class);
        HostDinnerViewDto dto = (HostDinnerViewDto) result;
        assertThat(dto.host().uuid()).isEqualTo(userId);
        assertThat(dto.storyTitle()).isEqualTo("Story Title");
        assertThat(dto.storyBannerUrl()).isEqualTo("banner.png");
        assertThat(dto.yourPrivateInfo().characterId()).isEqualTo(characterId);
        assertThat(dto.yourPrivateInfo().privateDescription()).isEqualTo("private");

        assertThat(dto.participants()).hasSize(2);
        dto.participants().stream()
                .filter(p -> p.user() != null)
                .forEach(participantDto -> {
                    assertThat(participantDto.user().uuid()).isEqualTo(userId);
                    assertThat(participantDto.character()).isEqualTo(characterDetailDto);
                });

        assertThat(dto.assignments()).hasSize(2);
        boolean foundAssigned = false;
        boolean foundPending = false;
        for (CharacterAssignmentDto assignmentDto : dto.assignments()) {
            if (assignmentDto.characterId().equals(characterId)) {
                assertThat(assignmentDto.userId()).contains(userId);
                assertThat(assignmentDto.inviteCode()).isEmpty();
                foundAssigned = true;
            } else if (assignmentDto.characterId().equals(pendingCharacterId)) {
                assertThat(assignmentDto.userId()).isEmpty();
                assertThat(assignmentDto.inviteCode()).contains("INVITE456");
                foundPending = true;
            }
        }
        assertThat(foundAssigned).isTrue();
        assertThat(foundPending).isTrue();
    }

    @Test
    void getDinnerView_returnsGuestDinnerView_whenUserIsGuest() throws Exception {
        String oauthId = "guest-oauth";
        UUID dinnerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID characterId = UUID.randomUUID();

        User guest = User.builder().id(userId).oauthId(oauthId).name("Guest").avatarUrl("avatar.png").build();
        User hostUser = User.builder().id(UUID.randomUUID()).oauthId("host-oauth").name("Host").build();
        story = Story.builder().title("Story Title").bannerUrl("banner.png").dinnerStoryBrief("brief").build();
        character = Character.builder().id(characterId).privateBriefing("private").build();
        CharacterAssignment assignment = CharacterAssignment.builder().user(guest).character(character).build();
        SessionParticipant participant = SessionParticipant.builder().user(guest).build();
        session = Session.builder()
                .id(dinnerId)
                .host(hostUser)
                .story(story)
                .startedAt(LocalDateTime.now())
                .characterAssignments(Set.of(assignment))
                .participants(Set.of(participant))
                .build();

        CharacterDetailDto characterDetailDto = mock(
                com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto.class);
        when(userService.getUserOrThrow(oauthId)).thenReturn(guest);
        when(sessionRepository.findById(dinnerId)).thenReturn(Optional.of(session));
        when(characterMapper.asDetailDTO(character)).thenReturn(characterDetailDto);
        when(userMapper.asDTO(guest)).thenReturn(new UserDto(userId, "Guest", "avatar.png"));
        when(userMapper.asDTO(hostUser))
                .thenReturn(new UserDto(hostUser.getId(), hostUser.getName(), hostUser.getAvatarUrl()));

        DinnerView result = sessionService.getDinnerView(oauthId, dinnerId);

        assertThat(result).isInstanceOf(GuestDinnerViewDto.class);
        GuestDinnerViewDto dto = (GuestDinnerViewDto) result;
        assertThat(dto.uuid()).isEqualTo(dinnerId);
        assertThat(dto.host().uuid()).isEqualTo(hostUser.getId());
        assertThat(dto.storyTitle()).isEqualTo("Story Title");
        assertThat(dto.storyBannerUrl()).isEqualTo("banner.png");
        assertThat(dto.yourPrivateInfo().characterId()).isEqualTo(characterId);
        assertThat(dto.yourPrivateInfo().privateDescription()).isEqualTo("private");

        assertThat(dto.participants()).hasSize(1);
        DinnerParticipantDto participantDto = dto.participants().iterator().next();
        assertThat(participantDto.user().uuid()).isEqualTo(userId);
        assertThat(participantDto.character()).isEqualTo(characterDetailDto);
    }
}