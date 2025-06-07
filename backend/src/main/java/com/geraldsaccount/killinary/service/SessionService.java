package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.NotAllowedException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.CreateSessionDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedSessionDto;
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
    private final UserMapper userMapper;

    public Set<DinnerSummaryDto> getSessionSummariesFrom(String oauthId) {
        Set<DinnerSummaryDto> usersDinners = sessionRepository.findAllByUserId(oauthId).stream()
                .map(session -> {
                    String characterName = session.getCharacterAssignments().stream()
                            .filter(a -> a.getUser().getOauthId().equals(oauthId))
                            .findFirst()
                            .map(a -> a.getCharacter().getName())
                            .orElse(null);
                    Story story = session.getStory();
                    return new DinnerSummaryDto(session.getId(),
                            session.getStartedAt(),
                            userMapper.asDTO(session.getHost()),
                            story.getTitle(),
                            story.getBannerUrl(),
                            characterName);
                })
                .collect(Collectors.toSet());

        return usersDinners;
    }

    @Transactional
    public CreatedSessionDto createSession(String oauthId, CreateSessionDto creationDTO)
            throws UserNotFoundException, StoryNotFoundException, StoryConfigurationNotFoundException,
            NotAllowedException {
        User host = userService.getUserOrThrow(oauthId);
        userService.validateHasNotPlayedStory(host, creationDTO.storyId());

        Story story = storyService.getStoryOrThrow(creationDTO.storyId());
        Session session = buildSession(host, story, creationDTO);
        session = addEmptyCharacterAssignment(session);
        return new CreatedSessionDto(session.getId());
    }

    private Session buildSession(User host, Story story, CreateSessionDto creationDTO)
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

}
