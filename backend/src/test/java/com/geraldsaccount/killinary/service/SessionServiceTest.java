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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.NotAllowedException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionCharacterAssignment;
import com.geraldsaccount.killinary.model.SessionParticipant;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.SessionCreationDTO;
import com.geraldsaccount.killinary.model.dto.output.SessionSummaryDTO;
import com.geraldsaccount.killinary.repository.SessionRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private SessionCodeService codeService;

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

        Set<SessionSummaryDTO> result = sessionService.getSessionSummariesFrom("U1");

        assertThat(result).isEmpty();
    }

    @Test
    void getSessionSummariesFrom_returnsSessionSummary_whenSessionExistsAndUserAssigned() {
        createDummySession();
        when(sessionRepository.findAllByUserId(user.getOauthId())).thenReturn(List.of(session));

        Set<SessionSummaryDTO> result = sessionService.getSessionSummariesFrom(user.getOauthId());

        assertThat(result).hasSize(1);
        SessionSummaryDTO dto = result.iterator().next();

        SessionSummaryDTO expected = new SessionSummaryDTO(session.getId(), host.getFirstName(), story.getTitle(),
                character.getName(), session.getStartedAt(), false);

        assertThat(dto)
                .isEqualTo(expected);
    }

    @Test
    void getSessionSummariesFrom_assignedCharacterNameIsNull_whenUserNotAssigned() {
        createDummySession();
        session.setCharacterAssignments(Collections.emptySet());
        when(sessionRepository.findAllByUserId(user.getOauthId())).thenReturn(List.of(session));

        Set<SessionSummaryDTO> result = sessionService.getSessionSummariesFrom(user.getOauthId());

        assertThat(result).hasSize(1);
        SessionSummaryDTO dto = result.iterator().next();

        SessionSummaryDTO expected = new SessionSummaryDTO(session.getId(), host.getFirstName(), story.getTitle(),
                null, session.getStartedAt(), false);

        assertThat(dto)
                .isEqualTo(expected);
    }

    @Test
    void getSessionSummariesFrom_isHostIsTrue_whenUserIsHost() {
        createDummySession();
        SessionCharacterAssignment assignment = SessionCharacterAssignment.builder()
                .character(character)
                .user(host)
                .build();
        session.setCharacterAssignments(Set.of(assignment));

        session.setCharacterAssignments(Collections.emptySet());
        when(sessionRepository.findAllByUserId(host.getOauthId())).thenReturn(List.of(session));

        Set<SessionSummaryDTO> result = sessionService.getSessionSummariesFrom(host.getOauthId());

        assertThat(result).hasSize(1);
        SessionSummaryDTO dto = result.iterator().next();

        SessionSummaryDTO expected = new SessionSummaryDTO(session.getId(), host.getFirstName(), story.getTitle(),
                null, session.getStartedAt(), true);

        assertThat(dto)
                .isEqualTo(expected);
    }

    @Test
    void createSession_throwsUserNotFound_withInvalidOathId() {
        String invalidId = "invalid-id";
        when(userRepository.findByOauthId(invalidId)).thenReturn(Optional.empty());
        SessionCreationDTO creationDTO = mock(SessionCreationDTO.class);

        assertThatThrownBy(() -> {
            sessionService.createSession(invalidId, creationDTO);
        }).isInstanceOf(UserNotFoundException.class)
                .hasMessage("Could not find host.");
    }

    @Test
    void createSession_throwsNotAllowed_whenHostAlreadyKnowsStory() {
        String validOauthId = "valid-oauth-id";
        UUID participatedId = UUID.randomUUID();
        User testHost = mock(User.class);
        SessionParticipant participant = mock(SessionParticipant.class);
        Session participatedSession = mock(Session.class);
        Story participatedStory = mock(Story.class);
        SessionCreationDTO creationDTO = SessionCreationDTO.builder()
                .storyId(participatedId)
                .build();
        when(userRepository.findByOauthId(validOauthId)).thenReturn(Optional.of(testHost));
        when(testHost.getSessionParticipations()).thenReturn(Set.of(participant));
        when(participant.getSession()).thenReturn(participatedSession);
        when(participatedSession.getStory()).thenReturn(participatedStory);
        when(participatedStory.getId()).thenReturn(participatedId);

        assertThatThrownBy(() -> sessionService.createSession(validOauthId, creationDTO))
                .isInstanceOf(NotAllowedException.class)
                .hasMessage("User cannot play a Story multiple times");
    }

    @Test
    void createSession_throwsStoryNotFound_withInvalidStoryId() {
        String validOauthId = "valid-oauth-id";
        host = mock(User.class);

        when(userRepository.findByOauthId(validOauthId)).thenReturn(Optional.of(host));
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyRepository.findById(any())).thenReturn(Optional.empty());
        SessionCreationDTO creationDTO = SessionCreationDTO.builder()
                .storyId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> {
            sessionService.createSession(validOauthId, creationDTO);
        }).isInstanceOf(StoryNotFoundException.class)
                .hasMessage("Could not find Story.");
    }

    @Test
    void createSession_throwsStoryConfigurationNotFound_withInvalidConfigurationId() {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        host = mock(User.class);
        story = mock(Story.class);
        StoryConfiguration config = mock(StoryConfiguration.class);

        when(userRepository.findByOauthId(validOauthId)).thenReturn(Optional.of(host));
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyRepository.findById(storyId)).thenReturn(Optional.of(story));
        when(story.getConfigurations()).thenReturn(Set.of(config));
        when(config.getId()).thenReturn(UUID.randomUUID());

        SessionCreationDTO creationDTO = SessionCreationDTO.builder()
                .storyId(storyId)
                .storyConfigurationId(UUID.randomUUID())
                .build();
        assertThatThrownBy(() -> {
            sessionService.createSession(validOauthId, creationDTO);
        }).isInstanceOf(StoryConfigurationNotFoundException.class)
                .hasMessage("Could not find Story Configuration");
    }

    @Test
    void createSession_throwsStoryConfigurationNotFound_withStoryContainingNoConfigs() {
        String validOauthId = "valid-oauth-id";
        UUID storyId = UUID.randomUUID();
        host = mock(User.class);
        story = mock(Story.class);

        when(userRepository.findByOauthId(validOauthId)).thenReturn(Optional.of(host));
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyRepository.findById(storyId)).thenReturn(Optional.of(story));
        when(story.getConfigurations()).thenReturn(Set.of());

        SessionCreationDTO creationDTO = SessionCreationDTO.builder()
                .storyId(storyId)
                .storyConfigurationId(UUID.randomUUID())
                .build();
        assertThatThrownBy(() -> {
            sessionService.createSession(validOauthId, creationDTO);
        }).isInstanceOf(StoryConfigurationNotFoundException.class)
                .hasMessage("Could not find Story Configuration");
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

        when(userRepository.findByOauthId(validOauthId)).thenReturn(Optional.of(host));
        when(host.getSessionParticipations()).thenReturn(Set.of());
        when(storyRepository.findById(storyId)).thenReturn(Optional.of(story));
        when(story.getConfigurations()).thenReturn(Set.of(config));
        when(config.getId()).thenReturn(configId);
        when(sessionRepository.save(any())).thenReturn(session);
        when(session.getId()).thenReturn(sessionId);

        SessionCreationDTO creationDTO = SessionCreationDTO.builder()
                .storyId(storyId)
                .storyConfigurationId(configId)
                .build();

        assertThat(sessionService.createSession(validOauthId, creationDTO).sessionId())
                .isEqualTo(sessionId);

        verify(sessionRepository, times(1)).save(any());
        verify(codeService, times(1)).generateCode();
    }

    private void createDummySession() {
        host = User.builder()
                .oauthId("UH")
                .firstName("Host")
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
        SessionCharacterAssignment assignment = SessionCharacterAssignment.builder()
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
}