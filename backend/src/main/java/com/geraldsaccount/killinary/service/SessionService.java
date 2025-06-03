package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.NotAllowedException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.SessionCreationDTO;
import com.geraldsaccount.killinary.model.dto.output.NewSessionDTO;
import com.geraldsaccount.killinary.model.dto.output.SessionSummaryDTO;
import com.geraldsaccount.killinary.repository.SessionRepository;
import com.geraldsaccount.killinary.repository.StoryRepository;
import com.geraldsaccount.killinary.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final SessionCodeService codeService;

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
        User host = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException("Could not find host."));

        boolean hasAlreadyPlayed = host.getSessionParticipations().stream()
                .anyMatch(s -> s.getSession().getStory().getId().equals(creationDTO.storyId()));

        if (hasAlreadyPlayed) {
            throw new NotAllowedException("User cannot play a Story multiple times");
        }

        Story story = storyRepository.findById(creationDTO.storyId())
                .orElseThrow(() -> new StoryNotFoundException("Could not find Story."));
        Session session = sessionRepository.save(Session.builder()
                .host(host)
                .startedAt(creationDTO.eventStart())
                .story(story)
                .status(SessionStatus.CREATED)
                .storyConfiguration(story.getConfigurations().stream()
                        .filter(conf -> conf.getId().equals(creationDTO.storyConfigurationId()))
                        .findFirst()
                        .orElseThrow(
                                () -> new StoryConfigurationNotFoundException("Could not find Story Configuration")))
                .code(codeService.generateCode())
                .build());
        return new NewSessionDTO(session.getId());
    }

}
