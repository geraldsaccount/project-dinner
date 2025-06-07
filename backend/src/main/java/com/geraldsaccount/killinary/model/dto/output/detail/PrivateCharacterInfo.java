package com.geraldsaccount.killinary.model.dto.output.detail;

import java.util.UUID;

public record PrivateCharacterInfo(
        UUID characterId,
        String privateDescription) {

}
