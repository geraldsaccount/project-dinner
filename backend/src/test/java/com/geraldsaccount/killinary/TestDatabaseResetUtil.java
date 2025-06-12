package com.geraldsaccount.killinary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.SessionRepository;
import com.geraldsaccount.killinary.repository.StoryConfigurationRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

@Component
public class TestDatabaseResetUtil {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private StoryConfigurationRepository storyConfigurationRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired(required = false)
    private CharacterAssignmentRepository characterAssignmentRepository;

    public void resetDatabase() {
        userRepository.findAll().forEach(user -> {
            user.getSessions().clear();
            userRepository.save(user);
        });
        sessionRepository.findAll().forEach(session -> {
            session.getParticipants().clear();
            sessionRepository.save(session);
        });

        // Delete in correct order
        if (characterAssignmentRepository != null)
            characterAssignmentRepository.deleteAll();
        sessionRepository.deleteAll();
        storyConfigurationRepository.deleteAll();
        characterRepository.deleteAll();
        storyRepository.deleteAll();
        userRepository.deleteAll();
    }
}