package com.geraldsaccount.killinary.service;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.NotAllowedException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterAssignmentService {
    private final UserService userService;
    private final CharacterAssignmentRepository repository;

    @Transactional
    public void acceptInvitation(String oautId, String inviteCode)
            throws UserNotFoundException, CharacterAssignmentNotFoundException, NotAllowedException {
        User user = userService.getUserOrThrow(oautId);

        CharacterAssignment assignment = repository.findByCode(inviteCode)
                .orElseThrow(() -> new CharacterAssignmentNotFoundException("Invalid invitation code"));

        userService.validateHasNotPlayedStory(user, assignment.getSession().getStory().getId());
        repository.save(assignment
                .withCode(null)
                .withUser(user));
    }

}
