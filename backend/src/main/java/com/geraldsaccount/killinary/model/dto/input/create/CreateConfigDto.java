package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;

public record CreateConfigDto(
        String id,
        Integer playerCount,
        List<String> characterIds) {

}
