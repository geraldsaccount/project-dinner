package com.geraldsaccount.killinary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;
import com.geraldsaccount.killinary.repository.CharacterRepository;
import com.geraldsaccount.killinary.repository.DinnerRepository;
import com.geraldsaccount.killinary.repository.MysteryRepository;
import com.geraldsaccount.killinary.repository.PlayerConfigRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

@Component
public class TestDatabaseResetUtil {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DinnerRepository dinnerRepository;
    @Autowired
    private MysteryRepository mysteryRepository;
    @Autowired
    private PlayerConfigRepository storyConfigurationRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired(required = false)
    private CharacterAssignmentRepository characterAssignmentRepository;

    public void resetDatabase() {
        userRepository.findAll().forEach(user -> {
            user.getDinners().clear();
            userRepository.save(user);
        });
        dinnerRepository.findAll().forEach(dinner -> {
            dinner.getParticipants().clear();
            dinnerRepository.save(dinner);
        });

        if (characterAssignmentRepository != null)
            characterAssignmentRepository.deleteAll();
        dinnerRepository.deleteAll();
        storyConfigurationRepository.deleteAll();
        characterRepository.deleteAll();
        mysteryRepository.deleteAll();

        userRepository.deleteAll();
    }
}