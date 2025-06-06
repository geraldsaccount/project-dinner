package com.geraldsaccount.killinary.model.dto.clerk;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthUserData {
    @JsonProperty("email_addresses")
    private ClerkEmailAddress[] emailAddresses;

    private String name;
    private String id;

    @JsonProperty("primary_email_address_id")
    private String primaryEmailAddressID;

    private String avatar;
}
