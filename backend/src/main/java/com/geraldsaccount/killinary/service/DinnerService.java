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
import com.geraldsaccount.killinary.exceptions.CodeGenerationException;
import com.geraldsaccount.killinary.exceptions.DinnerNotFoundException;
import com.geraldsaccount.killinary.exceptions.MysteryNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.CharacterMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dinner.DinnerStatus;
import com.geraldsaccount.killinary.model.dto.input.CreateDinnerDto;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.detail.PrivateCharacterInfo;
import com.geraldsaccount.killinary.model.dto.output.dinner.CharacterAssignmentDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerParticipantDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerView;
import com.geraldsaccount.killinary.model.dto.output.dinner.GuestDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.HostDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedDinnerDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.repository.DinnerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DinnerService {
    private final DinnerRepository dinnerRepository;
    private final UserService userService;
    private final MysteryService mysteryService;
    private final CharacterAssignmentCodeService assignmentCodeService;
    private final UserMapper userMapper;
    private final CharacterMapper characterMapper;

    @Transactional
    public Set<DinnerSummaryDto> getDinnerSummariesFrom(String oauthId) {
        return dinnerRepository.findAllByUserId(oauthId).stream()
                .map(dinner -> {
                    String characterName = dinner.getCharacterAssignments().stream()
                            .filter(a -> a.getUser().getOauthId().equals(oauthId))
                            .findFirst()
                            .map(a -> a.getCharacter().getName())
                            .orElse(null);
                    Mystery mystery = dinner.getMystery();
                    return new DinnerSummaryDto(dinner.getId(),
                            dinner.getDate(),
                            userMapper.asDTO(dinner.getHost()),
                            mystery.getStory().getTitle(),
                            mystery.getStory().getBannerUrl(),
                            characterName);
                })
                .collect(Collectors.toSet());
    }

    @Transactional
    public CreatedDinnerDto createDinner(String oauthId, CreateDinnerDto creationDTO)
            throws UserNotFoundException, MysteryNotFoundException,
            StoryConfigurationNotFoundException,
            AccessDeniedException {
        User host = userService.getUserOrThrow(oauthId);
        userService.validateHasNotPlayedStory(host, creationDTO.storyId());

        Mystery mystery = mysteryService.getMysteryOrThrow(creationDTO.storyId());
        Dinner dinner = buildDinner(host, mystery, creationDTO);
        dinner = addEmptyCharacterAssignment(dinner);
        return new CreatedDinnerDto(dinner.getId());
    }

    private Dinner buildDinner(User host, Mystery mystery, CreateDinnerDto creationDTO)
            throws StoryConfigurationNotFoundException {
        return dinnerRepository.save(Dinner.builder()
                .host(host)
                .date(creationDTO.eventStart())
                .mystery(mystery)
                .status(DinnerStatus.CREATED)
                .config(mystery.getSetups().stream()
                        .filter(conf -> conf.getId().equals(creationDTO.storyConfigurationId()))
                        .findFirst()
                        .orElseThrow(
                                () -> new StoryConfigurationNotFoundException("Could not find Story Configuration")))
                .build());
    }

    @Transactional
    private Dinner addEmptyCharacterAssignment(Dinner dinner) {
        int maxAttempts = 5;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Set<String> codes = new HashSet<>();
            Set<CharacterAssignment> assignments = dinner.getConfig().getCharacters().stream()
                    .map(character -> {
                        String code = "";
                        do {
                            code = assignmentCodeService.generateCode();
                        } while (codes.contains(code));
                        codes.add(code);
                        return CharacterAssignment.builder()
                                .dinner(dinner)
                                .character(character)
                                .code(code)
                                .build();
                    })
                    .collect(Collectors.toSet());
            try {
                return dinnerRepository.save(dinner.withCharacterAssignments(assignments));
            } catch (DataIntegrityViolationException e) {
                // continue when thrown
            }
        }
        throw new CodeGenerationException("Could not generate a unique code after " +
                maxAttempts + " attempts");
    }

    @Transactional
    public DinnerView getDinnerView(String oauthId, UUID dinnerId)
            throws UserNotFoundException, DinnerNotFoundException,
            AccessDeniedException,
            CharacterAssignmentNotFoundException {
        User user = userService.getUserOrThrow(oauthId);

        Dinner dinner = dinnerRepository.findById(dinnerId)
                .orElseThrow(() -> new DinnerNotFoundException("Could not find dinner"));

        boolean isInDinner = dinner.getCharacterAssignments().stream()
                .anyMatch(a -> a.getUser() != null && a.getUser().equals(user));

        if (!isInDinner) {
            throw new AccessDeniedException("Cannot access dinner data.");
        }

        boolean isHost = dinner.getHost().equals(user);
        Mystery mystery = dinner.getMystery();
        Set<CharacterAssignment> assignments = dinner.getCharacterAssignments();
        Set<DinnerParticipantDto> participants = assignments.stream()
                .map(a -> {
                    User participant = a.getUser();
                    UserDto userDto = participant == null ? null
                            : userMapper.asDTO(participant);

                    Character character = a.getCharacter();
                    CharacterDetailDto characterDto = characterMapper.asDetailDTO(character);

                    return new DinnerParticipantDto(userDto, characterDto);
                })
                .collect(Collectors.toSet());

        CharacterAssignment assignedCharacter = assignments.stream()
                .filter(a -> a.getUser() != null &&
                        a.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new CharacterAssignmentNotFoundException(
                        "User should have been assigned to a character."));
        PrivateCharacterInfo privateInfo = new PrivateCharacterInfo(assignedCharacter.getCharacter().getId(),
                assignedCharacter.getCharacter().getPrivateDescription());

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
                    dinner.getDate(),
                    userMapper.asDTO(user),
                    mystery.getStory().getTitle(),
                    mystery.getStory().getBannerUrl(),
                    mystery.getStory().getBriefing(),
                    participants,
                    privateInfo,
                    assignmentDtos,
                    null);
        }
        UserDto hostDto = userMapper.asDTO(dinner.getHost());
        return new GuestDinnerViewDto(dinnerId,
                dinner.getDate(),
                hostDto,
                mystery.getStory().getTitle(),
                mystery.getStory().getBannerUrl(),
                mystery.getStory().getBriefing(),
                participants,
                privateInfo);
    }

}
