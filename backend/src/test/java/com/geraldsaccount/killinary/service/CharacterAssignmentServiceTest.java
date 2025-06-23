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

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dto.output.other.InvitationViewDto;
import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;
import com.geraldsaccount.killinary.repository.DinnerRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class CharacterAssignmentServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private CharacterAssignmentRepository repository;
    @Mock
    private DinnerRepository dinnerRepository;
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
        Mystery mystery = new Mystery();
        mystery.setId(UUID.randomUUID());
        Dinner dinner = new Dinner();
        dinner.setMystery(mystery);
        CharacterAssignment assignment = mock(CharacterAssignment.class);

        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(assignment.getDinner()).thenReturn(dinner);
        when(assignment.withCode(null)).thenReturn(assignment);
        when(assignment.withUser(user)).thenReturn(assignment);

        service.acceptInvitation(oauthId, inviteCode);

        verify(userService).validateHasNotPlayedStory(user, mystery.getId());
        verify(repository).save(assignment);
        verify(dinnerRepository).save(any());
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
    void acceptInvitation_throwsAssignmentNotFound_whenAssignmentNotFound()
            throws UserNotFoundException {
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
        story.setShopDescription("desc");
        Mystery mystery = new Mystery();
        mystery.setId(UUID.randomUUID());
        mystery.setStory(story);
        Dinner dinner = new Dinner();
        dinner.setHost(host);
        dinner.setMystery(mystery);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getDinner()).thenReturn(dinner);
        when(assignment.getCharacter()).thenReturn(character);

        CharacterAssignment otherAssignment = mock(CharacterAssignment.class);
        when(otherAssignment.getCharacter()).thenReturn(character);
        when(otherAssignment.getUser()).thenReturn(user);

        Set<CharacterAssignment> assignments = new HashSet<>(Arrays.asList(assignment, otherAssignment));
        dinner.setCharacterAssignments(assignments);

        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDto.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDto.class));

        InvitationViewDto dto = service.getInvitation(auth, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.inviteCode()).isEqualTo(inviteCode);
        assertThat(dto.canAccept()).isTrue();
        verify(userService).validateHasNotPlayedStory(user, mystery.getId());
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
        Mystery mystery = new Mystery();
        mystery.setId(UUID.randomUUID());
        mystery.setStory(story);
        Dinner dinner = new Dinner();
        dinner.setHost(new User());
        dinner.setMystery(mystery);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getDinner()).thenReturn(dinner);
        when(assignment.getCharacter()).thenReturn(character);

        dinner.setCharacterAssignments(Set.of(assignment));
        when(userService.getUserOrThrow(oauthId)).thenReturn(user);
        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        doThrow(new AccessDeniedException("not allowed")).when(userService).validateHasNotPlayedStory(user,
                mystery.getId());

        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDto.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDto.class));

        InvitationViewDto dto = service.getInvitation(auth, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.canAccept()).isFalse();
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
        Mystery mystery = new Mystery();
        mystery.setId(UUID.randomUUID());
        mystery.setStory(new Story());
        Story story = new Story();
        story.setShopDescription("desc");
        Dinner dinner = new Dinner();
        dinner.setHost(host);
        dinner.setMystery(mystery);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getDinner()).thenReturn(dinner);
        when(assignment.getCharacter()).thenReturn(character);

        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDto.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDto.class));

        InvitationViewDto dto = service.getInvitation(auth, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.inviteCode()).isEqualTo(inviteCode);
        assertThat(dto.canAccept()).isTrue();
        verify(userService, never()).validateHasNotPlayedStory(any(), any());
    }

    @Test
    void getInvitation_returnsDTO_whenAuthenticationNull() throws Exception {
        String oauthId = "user1";

        String inviteCode = "invite123";
        User host = new User();
        Story story = new Story();
        story.setShopDescription("desc");
        Mystery mystery = new Mystery();
        mystery.setId(UUID.randomUUID());
        mystery.setStory(story);
        Dinner dinner = new Dinner();
        dinner.setHost(host);
        dinner.setMystery(mystery);

        Character character = new Character();
        CharacterAssignment assignment = mock(CharacterAssignment.class);
        when(assignment.getDinner()).thenReturn(dinner);
        when(assignment.getCharacter()).thenReturn(character);

        when(repository.findByCode(inviteCode)).thenReturn(Optional.of(assignment));
        when(characterMapper.asSummaryDTO(any())).thenReturn(mock(CharacterSummaryDto.class));
        when(userMapper.asDTO(any())).thenReturn(mock(UserDto.class));

        InvitationViewDto dto = service.getInvitation(null, inviteCode);

        assertThat(dto).isNotNull();
        assertThat(dto.inviteCode()).isEqualTo(inviteCode);
        assertThat(dto.canAccept()).isTrue();
        verify(userService, never()).validateHasNotPlayedStory(any(), any());
    }
}