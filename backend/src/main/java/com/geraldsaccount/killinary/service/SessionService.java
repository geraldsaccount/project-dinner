package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.SessionNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.model.SessionStatus;
import com.geraldsaccount.killinary.model.Story;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.input.CreateSessionDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.detail.PrivateCharacterInfo;
import com.geraldsaccount.killinary.model.dto.output.dinner.CharacterAssignmentDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerParticipantDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerView;
import com.geraldsaccount.killinary.model.dto.output.dinner.GuestDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.HostDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedSessionDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;
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
    private final CharacterMapper characterMapper;

    public Set<DinnerSummaryDto> getSessionSummariesFrom(String oauthId) {
        return sessionRepository.findAllByUserId(oauthId).stream()
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
    }

    @Transactional
    public CreatedSessionDto createSession(String oauthId, CreateSessionDto creationDTO)
            throws UserNotFoundException, StoryNotFoundException, StoryConfigurationNotFoundException,
            AccessDeniedException {
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
            }
        }
        throw new RuntimeException("Could not generate a unique code after " + maxAttempts + " attempts");
    }

    public DinnerView getDinnerView(String oauthId, UUID dinnerId)
            throws UserNotFoundException, SessionNotFoundException, AccessDeniedException,
            CharacterAssignmentNotFoundException {
        User user = userService.getUserOrThrow(oauthId);

        Session session = sessionRepository.findById(dinnerId)
                .orElseThrow(() -> new SessionNotFoundException("Could not find session"));

        boolean isInSession = session.getParticipants().stream().anyMatch(p -> p.getUser().equals(user));

        if (!isInSession) {
            throw new AccessDeniedException("Cannot access session data.");
        }

        boolean isHost = session.getHost().equals(user);
        Story story = session.getStory();
        Set<CharacterAssignment> assignments = session.getCharacterAssignments();
        Set<DinnerParticipantDto> participants = assignments.stream()
                .map(a -> {
                    User participant = a.getUser();
                    UserDto userDto = participant == null ? null
                            : userMapper.asDTO(participant);

                    Character character = a.getCharacter();
                    CharacterDetailDto characterDto = characterMapper.asDetailDTO(character);
                    ;

                    return new DinnerParticipantDto(userDto, characterDto);
                })
                .collect(Collectors.toSet());

        CharacterAssignment assignedCharacter = assignments.stream()
                .filter(a -> a.getUser() != null && a.getUser().getId().equals(user.getId())).findFirst()
                .orElseThrow(() -> new CharacterAssignmentNotFoundException(
                        "User should have been assigned to a character."));
        PrivateCharacterInfo privateInfo = new PrivateCharacterInfo(assignedCharacter.getCharacter().getId(),
                assignedCharacter.getCharacter().getPrivateBriefing());

        if (isHost) {
            Set<CharacterAssignmentDto> assignmentDtos = assignments.stream()
                    .map(a -> {
                        boolean isAssigned = a.getUser() != null;
                        return new CharacterAssignmentDto(
                                a.getCharacter().getId(),
                                isAssigned ? Optional.of(a.getUser().getId()) : Optional.empty(),
                                isAssigned ? Optional.empty() : Optional.of(a.getCode()));
                    })
                    .collect(Collectors.toSet());

            return new HostDinnerViewDto(dinnerId,
                    session.getStartedAt(),
                    userMapper.asDTO(user),
                    story.getTitle(),
                    story.getBannerUrl(),
                    story.getDinnerStoryBrief(),
                    participants,
                    privateInfo,
                    assignmentDtos,
                    null);
        }
        User host = session.getHost();
        UserDto hostDto = userMapper.asDTO(session.getHost());
        return new GuestDinnerViewDto(dinnerId,
                session.getStartedAt(),
                hostDto,
                story.getTitle(),
                story.getBannerUrl(),
                story.getDinnerStoryBrief(),
                participants,
                privateInfo);
    }

}
