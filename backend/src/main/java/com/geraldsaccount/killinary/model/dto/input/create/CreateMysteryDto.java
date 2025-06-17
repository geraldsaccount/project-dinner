package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;

import lombok.With;

@With
public record CreateMysteryDto(
        CreateStoryDto story,
        List<CreateCharacterDto> characters,
        List<CreateStageDto> stages,
        List<CreateConfigDto> setups,
        CreateCrimeDto crime) {

}
