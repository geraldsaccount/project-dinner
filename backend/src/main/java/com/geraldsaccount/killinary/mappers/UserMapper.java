package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkEmailAddress;
import com.geraldsaccount.killinary.model.dto.clerk.OAuthUserData;

@Component
public class UserMapper {

    public User withUpdatedClerkUserData(User source, User updated) {
        return source.withFirstName(updated.getFirstName())
                .withLastName(updated.getLastName())
                .withUsername(updated.getUsername())
                .withEmail(updated.getEmail());
    }

    public User fromClerkUser(OAuthUserData oauthUser) throws UserMapperException {
        String primaryEmail = getPrimaryEmail(oauthUser);

        if (oauthUser.getId() == null || primaryEmail == null) {
            throw new UserMapperException("Missing required user fields in webhook payload");
        }

        return User.builder()
                .oauthId(oauthUser.getId())
                .firstName(oauthUser.getFirstName())
                .lastName(oauthUser.getLastName())
                .email(getPrimaryEmail(oauthUser))
                .username(oauthUser.getUsername())
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
}
