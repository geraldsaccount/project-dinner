package com.geraldsaccount.killinary.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionCharacterAssignment;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.output.SessionSummaryDTO;
import com.geraldsaccount.killinary.repository.SessionRepository;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

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
        assertThat(dto.sessionId()).isEqualTo(session.getId());
        assertThat(dto.hostName()).isEqualTo(host.getFirstName());
        assertThat(dto.assignedCharacterName()).isEqualTo(character.getName());
        assertThat(dto.storyName()).isEqualTo(story.getTitle());
        assertThat(dto.sessionDate()).isEqualTo(session.getStartedAt());
    }

    @Test
    void getSessionSummariesFrom_assignedCharacterNameIsNull_whenUserNotAssigned() {
        createDummySession();
        session.setCharacterAssignments(Collections.emptySet());
        when(sessionRepository.findAllByUserId(user.getOauthId())).thenReturn(List.of(session));

        Set<SessionSummaryDTO> result = sessionService.getSessionSummariesFrom(user.getOauthId());

        assertThat(result).hasSize(1);
        SessionSummaryDTO dto = result.iterator().next();
        assertThat(dto.assignedCharacterName()).isNull();
        assertThat(dto.sessionId()).isEqualTo(session.getId());
        assertThat(dto.hostName()).isEqualTo(host.getFirstName());
        assertThat(dto.storyName()).isEqualTo(story.getTitle());
        assertThat(dto.sessionDate()).isEqualTo(session.getStartedAt());
    }

    private void createDummySession() {
        host = User.builder()
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