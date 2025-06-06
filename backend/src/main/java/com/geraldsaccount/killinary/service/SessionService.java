package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.NotAllowedException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.SessionCreationDTO;
import com.geraldsaccount.killinary.model.dto.output.NewSessionDTO;
import com.geraldsaccount.killinary.model.dto.output.SessionSummaryDTO;
import com.geraldsaccount.killinary.repository.SessionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final StoryService storyService;
    private final CharacterAssignmentCodeService assignmentCodeService;

    public Set<SessionSummaryDTO> getSessionSummariesFrom(String oauthId) {
        Set<SessionSummaryDTO> usersSessions = new HashSet<>();

        sessionRepository.findAllByUserId(oauthId).stream()
                .map(session -> {
                    String characterName = session.getCharacterAssignments().stream()
                            .filter(a -> a.getUser().getOauthId().equals(oauthId))
                            .findFirst()
                            .map(a -> a.getCharacter().getName())
                            .orElse(null);

                    return SessionSummaryDTO.builder()
                            .sessionId(session.getId())
                            .hostName(session.getHost().getFirstName())
                            .storyName(session.getStory().getTitle())
                            .assignedCharacterName(characterName)
                            .sessionDate(session.getStartedAt())
                            .isHost(session.getHost().getOauthId().equals(oauthId))
                            .build();
                })
                .forEach(usersSessions::add);

        return usersSessions;
    }

    @Transactional
    public NewSessionDTO createSession(String oauthId, SessionCreationDTO creationDTO)
            throws UserNotFoundException, StoryNotFoundException, StoryConfigurationNotFoundException,
            NotAllowedException {
        User host = userService.getUserOrThrow(oauthId);
        validateUserHasNotPlayedStory(host, creationDTO.storyId());

        Story story = storyService.getStoryByIdOrThrow(creationDTO.storyId());
        Session session = buildSession(host, story, creationDTO);
        session = addEmptyCharacterAssignment(session);
        return new NewSessionDTO(session.getId());

    }

    public String assignToCharacter(String oauthId, String code) {
        return "";
    }

    private Session buildSession(User host, Story story, SessionCreationDTO creationDTO)
            throws StoryConfigurationNotFoundException {
        return sessionRepository.save(Session.builder()
                .host(host)
                .startedAt(creationDTO.eventStart())
                .story(story)
                .status(SessionStatus.CREATED)
                .storyConfiguration(story.getConfigurations().stream()
                        .filter(conf -> conf.getId().equals(creationDTO.storyConfigurationId()))
                        .findFirst()
                        .orElseThrow(
                                () -> new StoryConfigurationNotFoundException("Could not find Story Configuration")))
                .build());
    }

    private Session addEmptyCharacterAssignment(Session session) {
        int maxAttempts = 5;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Set<String> codes = new HashSet<>();
            Set<CharacterAssignment> assignments = session.getStoryConfiguration().getCharactersInConfig().stream()
                    .map(confChar -> {
                        String code = "";
                        do {
                            code = assignmentCodeService.generateCode();
                        } while (codes.contains(code));
                        codes.add(code);
                        return CharacterAssignment.builder()
                                .session(session)
                                .code(code)
                                .build();
                    })
                    .collect(Collectors.toSet());
            try {
                return sessionRepository.save(session.withCharacterAssignments(assignments));
            } catch (DataIntegrityViolationException e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Could not generate a unique code after " +
                            maxAttempts + " attempts",
                            e);
                }
            }
        }
        throw new IllegalStateException("Should not reach here");
    }

    private void validateUserHasNotPlayedStory(User user, UUID storyId) throws NotAllowedException {
        boolean hasAlreadyPlayed = user.getSessionParticipations().stream()
                .anyMatch(s -> s.getSession().getStory().getId().equals(storyId));

        if (hasAlreadyPlayed) {
            throw new NotAllowedException("User cannot play a Story multiple times");
        }
    }
}
