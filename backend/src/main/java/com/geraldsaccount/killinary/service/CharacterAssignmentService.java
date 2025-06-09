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
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerParticipantDto;
import com.geraldsaccount.killinary.model.dto.output.other.InvitationViewDto;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterAssignmentService {
    private final UserService userService;
    private final CharacterAssignmentRepository repository;
    private final CharacterMapper characterMapper;
    private final UserMapper userMapper;

    @Transactional
    public void acceptInvitation(String oauthId, String inviteCode)
            throws UserNotFoundException, CharacterAssignmentNotFoundException, AccessDeniedException {
        User user = userService.getUserOrThrow(oauthId);

        CharacterAssignment assignment = repository.findByCode(inviteCode)
                .orElseThrow(() -> new CharacterAssignmentNotFoundException("Invalid invitation code"));

        userService.validateHasNotPlayedStory(user, assignment.getSession().getStory().getId());
        repository.save(assignment
                .withCode(null)
                .withUser(user));
    }

    public InvitationViewDto getInvitation(Authentication auth, String inviteCode)
            throws UserNotFoundException, CharacterAssignmentNotFoundException {
        boolean canAccept = true;
        CharacterAssignment assignment = repository.findByCode(inviteCode)
                .orElseThrow(() -> new CharacterAssignmentNotFoundException("Invalid invitation code"));
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.getUserOrThrow(auth.getName());

            try {
                userService.validateHasNotPlayedStory(user, assignment.getSession().getStory().getId());
            } catch (AccessDeniedException e) {
                canAccept = false;
            }
        }

        Session session = assignment.getSession();
        Story story = session.getStory();

        Set<DinnerParticipantDto> otherParticipants = session.getCharacterAssignments().stream()
                .map(charAs -> new DinnerParticipantDto(userMapper.asDTO(charAs.getUser()), null))
                .collect(Collectors.toSet());

        return new InvitationViewDto(inviteCode,
                session.getStartedAt(),
                userMapper.asDTO(session.getHost()),
                story.getTitle(),
                story.getBannerUrl(),
                story.getDinnerStoryBrief(),
                characterMapper.asDetailDTO(assignment.getCharacter()),
                otherParticipants,
                canAccept);
    }

}
