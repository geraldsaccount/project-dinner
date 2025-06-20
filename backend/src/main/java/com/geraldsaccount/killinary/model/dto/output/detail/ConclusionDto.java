package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.List;
import java.util.UUID;

public record ConclusionDto(
        List<UUID> criminalIds,
        String motive,
        List<FinalVoteDto> votes) {

}
