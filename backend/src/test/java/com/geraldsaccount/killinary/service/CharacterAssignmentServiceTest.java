package com.geraldsaccount.killinary.service;

import java.util.Arrays;
import java.util.HashSet;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.NotAllowedException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.output.CharacterSummaryDTO;
import com.geraldsaccount.killinary.model.dto.output.InvitationDTO;
import com.geraldsaccount.killinary.model.dto.output.UserDTO;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class CharacterAssignmentServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private CharacterAssignmentRepository repository;
    @Mock
    private CharacterMapper characterMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private CharacterAssignmentService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void acceptInvitation_assignsUserAndClearCode_whenValid() throws Exception {
        String oauthId = "user1";
        String inviteCode = "invite123";
        User user = new User();
        Story story = new Story();
        story.setId(UUID.randomUUID());
        Session session = new Session();
        session.setStory(story);
        CharacterAssignment assignment = mock(CharacterAssignment.class);

        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(assignment.getSession()).thenReturn(session);
        when(assignment.withCode(null)).thenReturn(assignment);
        when(assignment.withUser(user)).thenReturn(assignment);

        service.acceptInvitation(oauthId, inviteCode);

        verify(userService).validateHasNotPlayedStory(user, story.getId());
        verify(repository).save(assignment);
    }

    @Test
    void acceptInvitation_throwsUserNotFound_whenUserNotFound() throws UserNotFoundException {
        String oauthId = "user1";
        String inviteCode = "invite123";
        when(userService.getUserOrThrow(oauthId)).thenThrow(new UserNotFoundException("not found"));

        assertThatThrownBy(() -> service.acceptInvitation(oauthId, inviteCode))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void acceptInvitation_throwsAssignmentNotFound_whenAssignmentNotFound() throws UserNotFoundException {
        String oauthId = "user1";
        String inviteCode = "invite123";
        User user = new User();
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(repository.findByCode(inviteCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.acceptInvitation(oauthId, inviteCode))
                .isInstanceOf(CharacterAssignmentNotFoundException.class);
    }

    @Test
    void getInvitation_returnsDTO_whenValidAndCanAccept() throws Exception {
        String oauthId = "user1";
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn(oauthId);

        String inviteCode = "invite123";
        User user = new User();
        User host = new User();
        Story story = new Story();
        story.setId(UUID.randomUUID());
        story.setShopDescription("desc");
        Session session = new Session();
        session.setHost(host);
        session.setStory(story);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getSession()).thenReturn(session);
        when(assignment.getCharacter()).thenReturn(character);

        CharacterAssignment otherAssignment = mock(CharacterAssignment.class);
        when(otherAssignment.getCharacter()).thenReturn(character);
        when(otherAssignment.getUser()).thenReturn(user);

        Set<CharacterAssignment> assignments = new HashSet<>(Arrays.asList(assignment, otherAssignment));
        session.setCharacterAssignments(assignments);

        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDTO.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDTO.class));

        InvitationDTO dto = service.getInvitation(auth, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.code()).isEqualTo(inviteCode);
        assertThat(dto.canJoin()).isTrue();
        verify(userService).validateHasNotPlayedStory(user, story.getId());
    }

    @Test
    void getInvitation_returnDTO_whenNotAllowed() throws Exception {
        String oauthId = "user1";
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn(oauthId);

        String inviteCode = "invite123";
        User user = new User();
        Story story = new Story();
        story.setId(UUID.randomUUID());
        Session session = new Session();
        session.setHost(new User());
        session.setStory(story);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getSession()).thenReturn(session);
        when(assignment.getCharacter()).thenReturn(character);

        session.setCharacterAssignments(Set.of(assignment));
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        doThrow(new NotAllowedException("not allowed")).when(userService).validateHasNotPlayedStory(user,
                story.getId());

        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDTO.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDTO.class));

        InvitationDTO dto = service.getInvitation(auth, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.canJoin()).isFalse();
    }

    @Test
    void getInvitation_throwsUserNotFound_whenUserNotFound() throws UserNotFoundException {
        when(repository.findByCode(any())).thenReturn(Optional.of(mock(CharacterAssignment.class)));

        String oauthId = "user1";
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn(oauthId);

        String inviteCode = "invite123";
        when(userService.getUserOrThrow(oauthId)).thenThrow(new UserNotFoundException("not found"));

        assertThatThrownBy(() -> service.getInvitation(auth, inviteCode))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getInvitation_throwsAssignmentNotFound_whenAssignmentNotFound() throws UserNotFoundException {
        Authentication auth = mock(Authentication.class);
        String inviteCode = "invite123";
        when(repository.findByCode(inviteCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getInvitation(auth, inviteCode))
                .isInstanceOf(CharacterAssignmentNotFoundException.class);
    }

    @Test
    void getInvitation_returnsDTO_whenNotAuthenticated() throws Exception {
        String oauthId = "user1";
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        String inviteCode = "invite123";
        User host = new User();
        Story story = new Story();
        story.setId(UUID.randomUUID());
        story.setShopDescription("desc");
        Session session = new Session();
        session.setHost(host);
        session.setStory(story);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getSession()).thenReturn(session);
        when(assignment.getCharacter()).thenReturn(character);

        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDTO.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDTO.class));

        InvitationDTO dto = service.getInvitation(auth, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.code()).isEqualTo(inviteCode);
        assertThat(dto.canJoin()).isTrue();
        verify(userService, never()).validateHasNotPlayedStory(any(), any());
    }

    @Test
    void getInvitation_returnsDTO_whenAuthenticationNull() throws Exception {
        String oauthId = "user1";

        String inviteCode = "invite123";
        User host = new User();
        Story story = new Story();
        story.setId(UUID.randomUUID());
        story.setShopDescription("desc");
        Session session = new Session();
        session.setHost(host);
        session.setStory(story);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getSession()).thenReturn(session);
        when(assignment.getCharacter()).thenReturn(character);

        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDTO.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDTO.class));

        InvitationDTO dto = service.getInvitation(null, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.code()).isEqualTo(inviteCode);
        assertThat(dto.canJoin()).isTrue();
        verify(userService, never()).validateHasNotPlayedStory(any(), any());
    }
}