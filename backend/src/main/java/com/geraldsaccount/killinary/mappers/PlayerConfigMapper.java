package com.geraldsaccount.killinary.mappers;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.dto.output.other.ConfigDto;
import com.geraldsaccount.killinary.model.mystery.Character;
import com.geraldsaccount.killinary.model.mystery.Gender;
import com.geraldsaccount.killinary.model.mystery.PlayerConfig;

@Component
public class PlayerConfigMapper {
    public ConfigDto asSummaryDTO(PlayerConfig input) {
        Set<Character> characters = input.getCharacters();
        Set<UUID> characterIds = characters.stream()
                .map(c -> c.getId())
                .collect(Collectors.toSet());

        Map<Gender, Long> genderCounts = characters.stream()
                .collect(Collectors.groupingBy(Character::getGender,
                        Collectors.counting()));
        Map<Gender, Integer> genderCountsInt = new EnumMap<>(Gender.class);
        genderCounts.forEach((k, v) -> genderCountsInt.put(k, v.intValue()));

        return new ConfigDto(input.getId(),
                input.getCharacters().size(),
                genderCountsInt,
                characterIds);
    }
}