package com.geraldsaccount.killinary.model.dto.clerk;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClerkUserPayload {
    private OAuthUserData data;
    private String type;
}
