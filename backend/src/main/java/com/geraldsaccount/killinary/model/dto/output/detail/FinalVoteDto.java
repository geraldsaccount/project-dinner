package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.UUID;

public record FinalVoteDto(
        UUID guestId,
        UUID suspectId,
        String motive) {

}
