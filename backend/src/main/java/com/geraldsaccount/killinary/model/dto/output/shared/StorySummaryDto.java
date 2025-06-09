package com.geraldsaccount.killinary.model.dto.output.shared;

import java.util.UUID;

public record StorySummaryDto(
        UUID uuid,
        String title,
        String bannerUrl,
        String thumbnailDescription) {

}
