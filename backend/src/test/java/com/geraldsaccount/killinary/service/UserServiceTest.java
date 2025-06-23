package com.geraldsaccount.killinary.service;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.repository.UserRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() throws Exception {
        String clerkId = "clerk123";
        User user = new User();
        when(userRepository.findByOauthId(clerkId)).thenReturn(Optional.of(user));

        userService.deleteUser(clerkId);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {
        String clerkId = "notfound";
        when(userRepository.findByOauthId(clerkId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(clerkId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Could not find User with given ClerkID");
    }

    @Test
    void createUserData_shouldSaveUser() {
        User user = new User();

        userService.createUserData(user);

        verify(userRepository).save(user);
    }

    @Test
    void updateUserData_shouldUpdateUser_whenUserExists() throws Exception {
        UUID userId = UUID.randomUUID();
        String oauthId = "U1";
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setOauthId(oauthId);
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setOauthId(oauthId);
        User mappedUser = new User();
        mappedUser.setId(userId);
        mappedUser.setOauthId(oauthId);

        when(userRepository.findByOauthId(oauthId)).thenReturn(Optional.of(existingUser));
        when(userMapper.withUpdatedClerkUserData(existingUser, updatedUser)).thenReturn(mappedUser);

        userService.updateUserData(updatedUser);

        verify(userRepository).save(mappedUser);
    }

    @Test
    void updateUserData_shouldThrowException_whenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        User updatedUser = new User();
        updatedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUserData(updatedUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User could not be found.");
    }

    @Test
    void getUserOrThrow_returnsUser_whenUserExists() throws Exception {
        String oauthId = "oauth123";
        User user = new User();
        when(userRepository.findByOauthId(oauthId)).thenReturn(Optional.of(user));

        User result = userService.getUserOrThrow(oauthId);

        assertThat(result).isEqualTo(user);
        verify(userRepository).findByOauthId(oauthId);
    }

    @Test
    void getUserOrThrow_throwsUsernotFound_whenUserDoesNotExist() {
        String oauthId = "missing";
        when(userRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserOrThrow(oauthId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Could not find user.");
    }
}