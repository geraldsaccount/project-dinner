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

	@JsonProperty("last_name")
	private String lastName;

	private String username;

	private String id;

	@JsonProperty("primary_email_address_id")
	private String primaryEmailAddressID;

	@JsonProperty("profile_image_url")
	private String profileImageUrl;
}
