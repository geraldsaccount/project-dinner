package com.geraldsaccount.killinary.model.dto.output.dinner;

import com.geraldsaccount.killinary.model.dto.output.shared.CharacterSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;

public record DinnerParticipantDto(
        UserDto user,
        CharacterSummaryDto character) {

}
