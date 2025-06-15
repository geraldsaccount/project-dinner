package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;

public record CreateMysteryDto(
        boolean isPublished,
        CreateStoryDto story,
        List<CreateCharacterDto> characters,
        List<CreateStageDto> stages,
        List<CreateConfigDto> setups,
        CreateCrimeDto crime) {

}
