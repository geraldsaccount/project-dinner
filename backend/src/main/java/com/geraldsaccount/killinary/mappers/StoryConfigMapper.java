package com.geraldsaccount.killinary.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.model.Character;
import com.geraldsaccount.killinary.model.Gender;
import com.geraldsaccount.killinary.model.StoryConfiguration;
import com.geraldsaccount.killinary.model.dto.output.CharacterSummaryDTO;
import com.geraldsaccount.killinary.model.dto.output.StoryConfigSummaryDTO;

@Component
public class StoryConfigMapper {
    public StoryConfigSummaryDTO asSummaryDTO(StoryConfiguration input,
            Function<Character, CharacterSummaryDTO> characterMapper) {
        Set<CharacterSummaryDTO> characters = input.getCharactersInConfig().stream()
                .map(c -> characterMapper.apply(c.getCharacter()))
                .collect(Collectors.toSet());

        Map<Gender, Long> genderCounts = characters.stream()
                .collect(Collectors.groupingBy(CharacterSummaryDTO::gender,
                        Collectors.counting()));
        Map<Gender, Integer> genderCountsInt = new HashMap<>();
        genderCounts.forEach((k, v) -> genderCountsInt.put(k, v.intValue()));

        return StoryConfigSummaryDTO.builder()
                .id(input.getId())
                .playerCount(input.getPlayerCount())
                .genderCounts(genderCountsInt)
                .characters(characters)
                .build();
    }
}
