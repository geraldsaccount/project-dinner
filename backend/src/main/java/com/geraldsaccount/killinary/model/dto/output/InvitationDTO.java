package com.geraldsaccount.killinary.model.dto.output;

public record InvitationDTO(String code,
        SessionPreviewDTO sessionPreview,
        CharacterSummaryDTO character,
        Boolean canJoin) {

}
