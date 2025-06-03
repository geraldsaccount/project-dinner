package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.model.dto.output.SessionSummaryDTO;
import com.geraldsaccount.killinary.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

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

}
