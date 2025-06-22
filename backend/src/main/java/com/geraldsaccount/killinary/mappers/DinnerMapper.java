package com.geraldsaccount.killinary.mappers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dinner.DinnerStatus;
import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.detail.ConclusionDto;
import com.geraldsaccount.killinary.model.dto.output.detail.FinalVoteDto;
import com.geraldsaccount.killinary.model.dto.output.detail.HostInfoDto;
import com.geraldsaccount.killinary.model.dto.output.detail.PrivateInfoDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.CharacterAssignmentDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.CharacterStageDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerParticipantDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.PreDinnerInfoDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.StageEventDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.CharacterStageInfo;
import com.geraldsaccount.killinary.model.mystery.Crime;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.Stage;
import com.geraldsaccount.killinary.model.mystery.Story;
import com.geraldsaccount.killinary.utils.ImageConverter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DinnerMapper {

  private final UserMapper userMapper;
  private final CharacterMapper characterMapper;

  public PreDinnerInfoDto getPreDinnerInfo(Dinner dinner) {
    Mystery mystery = dinner.getMystery();
    Story story = mystery.getStory();
    Set<CharacterAssignment> assignments = dinner.getCharacterAssignments();
    Set<DinnerParticipantDto> participants = assignments.stream()
        .map(a -> {
          User participant = a.getUser();
          UserDto userDto = participant == null ? null : userMapper.asDTO(participant);
          Character character = a.getCharacter();
          CharacterDetailDto characterDto = characterMapper.asDetailDTO(character);
          return new DinnerParticipantDto(userDto, characterDto);
        })
        .collect(Collectors.toSet());
    return new PreDinnerInfoDto(
        dinner.getId(),
        dinner.getDate(),
        userMapper.asDTO(dinner.getHost()),
        story.getTitle(),
        ImageConverter.imageAsBase64(story.getBannerImage()),
        story.getSetting(),
        story.getRules(),
        participants,
        dinner.getStatus());
  }

  public PrivateInfoDto getPrivateInfoForUser(User user, Dinner dinner) {
    Optional<CharacterAssignment> assignedCharacter = dinner.getCharacterAssignments().stream()
        .filter(a -> a.getUser() != null && a.getUser().getId().equals(user.getId()))
        .findFirst();
    PrivateInfoDto privateInfo = null;
    if (assignedCharacter.isPresent()) {
      Character character = assignedCharacter.get().getCharacter();
      privateInfo = getPrivateInfoForCharacter(character, dinner);
    }
    return privateInfo;
  }

  public PrivateInfoDto getPrivateInfoForCharacter(Character character, Dinner dinner) {
    List<CharacterStageDto> stages = null;
    if (dinner.getStatus() != DinnerStatus.CREATED) {
      List<CharacterStageInfo> stageInfoList = character.getStageInfo();

      stages = IntStream
          .range(0,
              Math.min(stageInfoList.size(),
                  (dinner.getCurrentStage() == null ? 0 : dinner.getCurrentStage()) + 1))
          .mapToObj(i -> {
            CharacterStageInfo s = stageInfoList.get(i);
            return new CharacterStageDto(
                dinner.getMystery().getStages().get(i).getTitle(),
                s.getObjectivePrompt(),
                s.getEvents().stream()
                    .map(e -> new StageEventDto(e.getTime(), e.getTitle(), e.getDescription()))
                    .toList());
          })
          .toList();
    }

    return new PrivateInfoDto(
        character.getId(),
        character.getPrivateDescription(),
        character.getRelationships(),
        stages);
  }

  public ConclusionDto getConclusion(Dinner dinner) {
    Crime crime = dinner.getMystery().getCrime();
    List<FinalVoteDto> votes = dinner.getSuspectVotes().stream()
        .map(v -> new FinalVoteDto(
            v.getUser().getId(),
            v.getSuspects().stream()
                .map(s -> s.getId())
                .toList(),
            v.getMotive()))
        .toList();

    if (dinner.getStatus() == DinnerStatus.VOTING) {
      return new ConclusionDto(true, null, null, votes);
    }

    if (dinner.getStatus() != DinnerStatus.CONCLUDED) {
      return null;
    }

    List<UUID> criminalIds = crime.getCriminals().stream()
        .map(c -> c.getId())
        .toList();
    return new ConclusionDto(false, criminalIds, crime.getDescription(), votes);
  }

  public HostInfoDto getHostInfo(Dinner dinner) {
    Mystery mystery = dinner.getMystery();

    Set<CharacterAssignment> assignments = dinner.getCharacterAssignments();
    Set<CharacterAssignmentDto> assignmentDtos = assignments.stream()
        .map(a -> {
          boolean isAssigned = a.getUser() != null;
          return new CharacterAssignmentDto(
              a.getCharacter().getId(),
              isAssigned ? Optional.of(a.getUser().getId()) : Optional.empty(),
              isAssigned ? Optional.empty() : Optional.of(a.getCode()));
        })
        .collect(Collectors.toSet());

    Set<PrivateInfoDto> missingPrivateInfos = null;
    List<String> stagePrompts = null;
    boolean allHaveVoted = false;
    if (dinner.getStatus() != DinnerStatus.CREATED) {
      missingPrivateInfos = assignments.stream()
          .filter(ass -> ass.getUser() == null)
          .map(ass -> getPrivateInfoForCharacter(ass.getCharacter(), dinner))
          .collect(Collectors.toSet());

      stagePrompts = mystery.getStages().stream()
          .limit((dinner.getCurrentStage() == null ? 0 : dinner.getCurrentStage()) + 1L)
          .map(Stage::getHostPrompt)
          .toList();
      allHaveVoted = dinner.getSuspectVotes().size() == assignments.stream()
          .filter(ass -> ass.getUser() != null)
          .count();
    }

    return new HostInfoDto(
        mystery.getStory().getBriefing(),
        assignmentDtos,
        missingPrivateInfos,
        stagePrompts,
        allHaveVoted);
  }
}
