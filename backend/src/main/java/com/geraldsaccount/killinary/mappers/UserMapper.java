package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkEmailAddress;
import com.geraldsaccount.killinary.model.dto.clerk.OAuthUserData;
import com.geraldsaccount.killinary.model.dto.output.shared.UserDto;

@Component
public class UserMapper {

	public User withUpdatedClerkUserData(User source, User updated) {
		return source.withName(updated.getName())
				.withAvatarUrl(updated.getAvatarUrl())
				.withEmail(updated.getEmail());
	}

	public User fromClerkUser(OAuthUserData oauthUser) throws UserMapperException {
		String primaryEmail = getPrimaryEmail(oauthUser);

		if (oauthUser.getId() == null || primaryEmail == null) {
			throw new UserMapperException("Missing required user fields in webhook payload");
		}

		String name = null;
		if (oauthUser.getUsername() != null && !oauthUser.getUsername().trim().isEmpty()) {
			name = oauthUser.getUsername();
		} else if (oauthUser.getFirstName() != null && !oauthUser.getFirstName().trim().isEmpty()) {
			String firstName = oauthUser.getFirstName().trim();
			String lastInitial = "";
			if (oauthUser.getLastName() != null && !oauthUser.getLastName().trim().isEmpty()) {
				lastInitial = " " + Character.toUpperCase(oauthUser.getLastName().trim().charAt(0)) + ".";
			}
			name = firstName + lastInitial;
		}

		return User.builder()
				.oauthId(oauthUser.getId())
				.name(name)
				.avatarUrl(oauthUser.getProfileImageUrl())
				.email(getPrimaryEmail(oauthUser))
				.build();
	}

	private String getPrimaryEmail(OAuthUserData oauthUser) {
		if (oauthUser.getPrimaryEmailAddressID() == null || oauthUser.getEmailAddresses() == null) {
			return null;
		}
		String primaryEmailId = oauthUser.getPrimaryEmailAddressID();
		for (ClerkEmailAddress email : oauthUser.getEmailAddresses()) {
			if (email.getId() != null && primaryEmailId.equals(email.getId())) {
				return email.getEmailAddress() != null ? email.getEmailAddress() : null;
			}
		}
		return null;
	}

	public UserDto asDTO(User user) {
		return new UserDto(user.getId(), user.getName(), user.getAvatarUrl());
	}
}
