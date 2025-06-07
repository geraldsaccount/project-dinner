package com.geraldsaccount.killinary.model.dto.output.other;

import java.time.LocalDateTime;
import java.util.Set;

import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerParticipantDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;

public record InvitationViewDto(
        String inviteCode,
        LocalDateTime dateTime,
        UserDto host,
        String storyTitle,
        String storyBannerUrl,
        String dinnerStoryBrief,
        CharacterDetailDto yourAssignedCharacter, // The character this invite is for
        Set<DinnerParticipantDto> otherParticipants, // "Starring" view of other players
        boolean canAccept) { // Backend determines if user has played story before

}
