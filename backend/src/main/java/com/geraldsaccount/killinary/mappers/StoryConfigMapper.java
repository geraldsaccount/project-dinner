package com.geraldsaccount.killinary.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.StoryConfigurationCharacter;
import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;

@Component
public class StoryConfigMapper {
    public ConfigDto asSummaryDTO(StoryConfiguration input) {
        Set<StoryConfigurationCharacter> charactersInConfig = input.getCharactersInConfig();
        Set<UUID> characterIds = charactersInConfig.stream()
                .map(c -> c.getCharacter().getId())
                .collect(Collectors.toSet());

        Map<Gender, Long> genderCounts = charactersInConfig.stream()
                .map(cic -> cic.getCharacter())
                .collect(Collectors.groupingBy(Character::getGender,
                        Collectors.counting()));
        Map<Gender, Integer> genderCountsInt = new HashMap<>();
        genderCounts.forEach((k, v) -> genderCountsInt.put(k, v.intValue()));

        return new ConfigDto(input.getId(),
                input.getPlayerCount(),
                genderCountsInt,
                characterIds);
    }
}
