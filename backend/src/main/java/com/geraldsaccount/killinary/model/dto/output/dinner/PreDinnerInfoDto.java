package com.geraldsaccount.killinary.model.dto.output.dinner;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;

public record PreDinnerInfoDto(
        UUID uuid,
        LocalDateTime dateTime,
        UserDto host,
        String storyTitle,
        String storyBannerData,
        String setting,
        String rules,
        Set<DinnerParticipantDto> participants) {

}
