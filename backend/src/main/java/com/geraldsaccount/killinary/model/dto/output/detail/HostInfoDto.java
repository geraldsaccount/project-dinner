package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.List;
import java.util.Set;

import com.geraldsaccount.killinary.model.dto.output.dinner.CharacterAssignmentDto;

public record HostInfoDto(
        String briefing,
        Set<CharacterAssignmentDto> assignments,
        Set<PrivateInfoDto> missingPrivateInfo,
        List<String> stagePrompts) {

}
