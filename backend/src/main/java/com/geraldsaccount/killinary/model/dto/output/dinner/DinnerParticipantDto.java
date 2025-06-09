package com.geraldsaccount.killinary.model.dto.output.dinner;

import com.geraldsaccount.killinary.model.dto.output.detail.CharacterDetailDto;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;

public record DinnerParticipantDto(
        UserDto user,
        CharacterDetailDto character) {

}
