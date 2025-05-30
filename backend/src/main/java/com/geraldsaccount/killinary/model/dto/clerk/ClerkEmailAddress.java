package com.geraldsaccount.killinary.model.dto.clerk;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClerkEmailAddress {
    @JsonProperty("email_address")
    private String emailAddress;
    private String id;
}
