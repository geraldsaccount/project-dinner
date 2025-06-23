package com.geraldsaccount.killinary.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.CharacterNotFoundException;
import com.geraldsaccount.killinary.exceptions.CodeGenerationException;
import com.geraldsaccount.killinary.exceptions.DinnerNotFoundException;
import com.geraldsaccount.killinary.exceptions.MysteryNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.DinnerMapper;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dinner.DinnerStatus;
import com.geraldsaccount.killinary.model.dinner.Vote;
import com.geraldsaccount.killinary.model.dto.input.CreateDinnerDto;
import com.geraldsaccount.killinary.model.dto.input.dinner.VoteDto;
import com.geraldsaccount.killinary.model.dto.output.detail.ConclusionDto;
import com.geraldsaccount.killinary.model.dto.output.detail.HostInfoDto;
import com.geraldsaccount.killinary.model.dto.output.detail.PrivateInfoDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerView;
import com.geraldsaccount.killinary.model.dto.output.dinner.GuestDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.HostDinnerViewDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.PreDinnerInfoDto;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedDinnerDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.repository.DinnerRepository;
import com.geraldsaccount.killinary.utils.ImageConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DinnerService {
    private final static String DINNER_NOT_FOUND_MESSAGE = "Could not find dinner";

    private final DinnerRepository dinnerRepository;
    private final UserService userService;
    private final MysteryService mysteryService;
    private final CharacterAssignmentCodeService assignmentCodeService;
    private final UserMapper userMapper;
    private final DinnerMapper dinnerMapper;

    @Transactional
    public Set<DinnerSummaryDto> getDinnerSummariesFrom(String oauthId) {
        return dinnerRepository.findAllByUserId(oauthId).stream()
                .map(dinner -> {
                    String characterName = dinner.getCharacterAssignments().stream()
                            .filter(a -> a.getUser() != null && a.getUser().getOauthId().equals(oauthId))
                            .findFirst()
                            .map(a -> a.getCharacter().getName())
                            .orElse("TBD");
                    Mystery mystery = dinner.getMystery();
                    return new DinnerSummaryDto(dinner.getId(),
                            dinner.getDate(),
                            userMapper.asDTO(dinner.getHost()),
                            mystery.getStory().getTitle(),
                            ImageConverter.imageAsBase64(mystery.getStory().getBannerImage()),
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
                .orElseThrow(() -> new DinnerNotFoundException(DINNER_NOT_FOUND_MESSAGE));

        boolean isHost = user.getId().equals(dinner.getHost().getId());
        boolean isInDinner = dinner.getCharacterAssignments().stream()
                .anyMatch(a -> a.getUser() != null && a.getUser().equals(user));

        if (!isInDinner && !isHost) {
            throw new AccessDeniedException("Cannot access dinner data.");
        }

        if (isHost) {
            return getHostDinnerView(user, dinner);
        }
        return getGuestDinnerView(user, dinner);
    }

    private GuestDinnerViewDto getGuestDinnerView(User user, Dinner dinner) {
        PreDinnerInfoDto preDinnerInfo = dinnerMapper.getPreDinnerInfo(dinner);
        PrivateInfoDto privateInfo = dinnerMapper.getPrivateInfoForUser(user, dinner);
        ConclusionDto conclusion = dinnerMapper.getConclusion(dinner);
        return new GuestDinnerViewDto(preDinnerInfo, privateInfo, conclusion);
    }

    private HostDinnerViewDto getHostDinnerView(User user, Dinner dinner) {
        PreDinnerInfoDto preDinnerInfo = dinnerMapper.getPreDinnerInfo(dinner);
        PrivateInfoDto privateInfo = dinnerMapper.getPrivateInfoForUser(user, dinner);
        ConclusionDto conclusion = dinnerMapper.getConclusion(dinner);
        HostInfoDto hostInfo = dinnerMapper.getHostInfo(dinner);
        return new HostDinnerViewDto(preDinnerInfo, privateInfo, conclusion, hostInfo);
    }

    @Transactional
    public DinnerView progressDinner(String oauthId, UUID dinnerId) throws UserNotFoundException,
            DinnerNotFoundException, CharacterAssignmentNotFoundException, AccessDeniedException {
        User user = userService.getUserOrThrow(oauthId);

        Dinner dinner = dinnerRepository.findById(dinnerId)
                .orElseThrow(() -> new DinnerNotFoundException(DINNER_NOT_FOUND_MESSAGE));

        boolean isHost = dinner.getHost().equals(user);

        if (!isHost) {
            throw new AccessDeniedException("Only host can progress dinner");
        }

        switch (dinner.getStatus()) {
            case DinnerStatus.CREATED -> {
                boolean allCharactersAssigned = dinner.getCharacterAssignments().stream()
                        .allMatch(cass -> cass.getUser() != null);
                if (!allCharactersAssigned) {
                    throw new AccessDeniedException("Dinner can only be started if all characters have been assigned.");
                }

                dinnerRepository.save(dinner.withStatus(DinnerStatus.IN_PROGRESS));
            }
            case DinnerStatus.IN_PROGRESS -> {
                int currentStage = dinner.getCurrentStage() == null ? 0 : dinner.getCurrentStage();
                boolean isLastStage = currentStage + 1 >= dinner.getMystery().getStages().size();

                if (isLastStage) {
                    dinnerRepository.save(dinner.withStatus(DinnerStatus.VOTING));
                } else {
                    dinnerRepository.save(dinner.withCurrentStage(currentStage + 1));
                }
            }
            case DinnerStatus.VOTING -> {
                boolean allGuestsVoted = dinner.getSuspectVotes().size() == dinner.getCharacterAssignments().stream()
                        .filter(cas -> cas.getUser() != null).count();

                if (allGuestsVoted) {
                    dinnerRepository.save(dinner.withStatus(DinnerStatus.CONCLUDED));
                } else {
                    throw new AccessDeniedException("Dinner can only be concluded if all guests have voted.");
                }
            }
            case DinnerStatus.CONCLUDED, DinnerStatus.CANCELED -> {
                // do nothing if dinner already concluded
            }
            default -> throw new AssertionError();
        }
        return getDinnerView(oauthId, dinnerId);
    }

    @Transactional
    public DinnerView castVote(String oauthId, UUID dinnerId, VoteDto vote) throws UserNotFoundException,
            DinnerNotFoundException, CharacterAssignmentNotFoundException, AccessDeniedException,
            CharacterNotFoundException {
        User user = userService.getUserOrThrow(oauthId);

        Dinner dinner = dinnerRepository.findById(dinnerId)
                .orElseThrow(() -> new DinnerNotFoundException(DINNER_NOT_FOUND_MESSAGE));

        boolean isInDinner = dinner.getCharacterAssignments().stream()
                .anyMatch(a -> a.getUser() != null && a.getUser().equals(user));

        if (!isInDinner) {
            throw new AccessDeniedException("User cannot cast vote for a dinner they are not participating in");
        }

        Set<Vote> votes = dinner.getSuspectVotes();
        if (votes.stream().anyMatch(v -> user.getId().equals(v.getUser().getId()))) {
            votes = votes.stream()
                    .filter(v -> !v.getUser().getId().equals(user.getId()))
                    .collect(Collectors.toSet());
        }

        List<Character> suspects = dinner.getMystery().getCharacters().stream()
                .filter(c -> vote.suspectIds().contains(c.getId()))
                .toList();

        if (suspects.size() == 0) {
            throw new CharacterNotFoundException("Could not find character with given id");
        }

        votes.add(Vote.builder()
                .user(user)
                .suspects(suspects)
                .motive(vote.motive())
                .build());

        dinnerRepository.save(dinner.withSuspectVotes(votes));

        return getDinnerView(oauthId, dinnerId);
    }

}
