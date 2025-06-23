package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.List;
import java.util.UUID;

public record FinalVoteDto(
    UUID guestId,
    List<UUID> suspectIds,
    String motive) {

}
