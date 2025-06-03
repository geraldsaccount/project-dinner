package com.geraldsaccount.killinary.model.dto.clerk;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthUserData {
    @JsonProperty("email_addresses")
    private ClerkEmailAddress[] emailAddresses;

    @JsonProperty("first_name")
    private String firstName;
    private String id;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("primary_email_address_id")
    private String primaryEmailAddressID;
    private String username;
}
