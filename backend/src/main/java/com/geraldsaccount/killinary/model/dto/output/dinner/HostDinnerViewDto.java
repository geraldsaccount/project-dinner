package com.geraldsaccount.killinary.model.dto.output.dinner;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.geraldsaccount.killinary.model.dto.output.detail.PrivateCharacterInfo;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;

public record HostDinnerViewDto(
        UUID uuid,
        LocalDateTime dateTime,
        UserDto host,
        String storyTitle,
        String storyBannerUrl,
        String dinnerStoryBrief,
        Set<DinnerParticipantDto> participants,

        // Host-specific information
        Set<CharacterAssignmentDto> assignments, // All assignments and invite codes
        Set<PrivateCharacterInfo> allPrivateInfo // All secrets, for host reference
// Object extraHostInformation // Placeholder for recipes, etc.
) {

}
