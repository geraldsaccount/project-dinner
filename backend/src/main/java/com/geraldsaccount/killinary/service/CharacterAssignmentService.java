package com.geraldsaccount.killinary.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerParticipantDto;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedDinnerDto;
import com.geraldsaccount.killinary.model.dto.output.other.InvitationViewDto;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;
import com.geraldsaccount.killinary.repository.DinnerRepository;
import com.geraldsaccount.killinary.utils.ImageConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterAssignmentService {
    private final UserService userService;
    private final CharacterAssignmentRepository repository;
    private final DinnerRepository dinnerRepository;
    private final CharacterMapper characterMapper;
    private final UserMapper userMapper;

    @Transactional
    public CreatedDinnerDto acceptInvitation(String oauthId, String inviteCode)
            throws UserNotFoundException, CharacterAssignmentNotFoundException,
            AccessDeniedException {
        User user = userService.getUserOrThrow(oauthId);

        CharacterAssignment assignment = repository.findByCode(inviteCode)
                .orElseThrow(() -> new CharacterAssignmentNotFoundException("Invalid invitation code"));

        Dinner dinner = assignment.getDinner();
        userService.validateHasNotPlayedStory(user,
                dinner.getMystery().getId());
        repository.save(assignment
                .withCode(null)
                .withUser(user));

        Set<User> participants = dinner.getParticipants();
        participants.add(user);
        dinnerRepository.save(dinner.withParticipants(participants));

        return new CreatedDinnerDto(assignment.getDinner().getId());
    }

    @Transactional
    public InvitationViewDto getInvitation(Authentication auth, String inviteCode)
            throws UserNotFoundException, CharacterAssignmentNotFoundException {
        boolean canAccept = true;
        CharacterAssignment assignment = repository.findByCode(inviteCode)
                .orElseThrow(() -> new CharacterAssignmentNotFoundException("Invalid invitation code"));
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.getUserOrThrow(auth.getName());

            try {
                userService.validateHasNotPlayedStory(user,
                        assignment.getDinner().getMystery().getId());
            } catch (AccessDeniedException e) {
                canAccept = false;
            }
        }

        Dinner dinner = assignment.getDinner();
        Mystery mystery = dinner.getMystery();
        Story story = mystery.getStory();

        Set<DinnerParticipantDto> otherParticipants = dinner.getCharacterAssignments().stream()
                .map(charAs -> new DinnerParticipantDto(userMapper.asDTO(charAs.getUser()),
                        characterMapper.asDetailDTO(charAs.getCharacter())))
                .collect(Collectors.toSet());

        return new InvitationViewDto(inviteCode,
                dinner.getDate(),
                userMapper.asDTO(dinner.getHost()),
                story.getTitle(),
                ImageConverter.imageAsBase64(story.getBannerImage()),
                story.getShopDescription(),
                characterMapper.asDetailDTO(assignment.getCharacter()),
                otherParticipants,
                canAccept);
    }

}
