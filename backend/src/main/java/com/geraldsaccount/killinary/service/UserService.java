package com.geraldsaccount.killinary.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public void deleteUser(String clerkId) throws UserNotFoundException {
        User foundUser = userRepository.findByOauthId(clerkId)
                .orElseThrow(() -> new UserNotFoundException("Could not find User with given ClerkID"));

        userRepository.delete(foundUser);
    }

    @Transactional
    public void createUserData(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void updateUserData(User updatedUser) throws UserNotFoundException {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User could not be found."));
        updatedUser = userMapper.withUpdatedClerkUserData(existingUser, updatedUser);

        userRepository.save(updatedUser);
    }

    public User getUserOrThrow(String oauthId) throws UserNotFoundException {
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException("Could not find user."));
    }

    public void validateHasNotPlayedStory(User user, UUID storyId) throws AccessDeniedException {
        boolean hasAlreadyPlayed = user.getSessions().stream()
                .anyMatch(s -> s.getStory().getId().equals(storyId));

        if (hasAlreadyPlayed) {
            throw new AccessDeniedException("User cannot play a Story multiple times");
        }
    }
}
