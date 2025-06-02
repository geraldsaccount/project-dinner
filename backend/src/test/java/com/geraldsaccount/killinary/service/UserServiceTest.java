package com.geraldsaccount.killinary.service;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.repository.UserRepository;

class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() throws Exception {
        String clerkId = "clerk123";
        User user = new User();
        when(userRepository.findByClerkId(clerkId)).thenReturn(Optional.of(user));

        userService.deleteUser(clerkId);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {
        String clerkId = "notfound";
        when(userRepository.findByClerkId(clerkId)).thenReturn(Optional.empty());

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
        User updatedUser = new User();
        updatedUser.setId(userId);
        User existingUser = new User();
        existingUser.setId(userId);
        User mappedUser = new User();
        mappedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
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
}