package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkEmailAddress;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkUserData;

@Component
public class UserMapper {

    public User withUpdatedUserData(User source, User updated) {
        return source.withFirstName(updated.getFirstName())
                .withLastName(updated.getLastName())
                .withUsername(updated.getUsername())
                .withEmail(updated.getEmail());
    }

    public User fromClerkUser(ClerkUserData clerkUser) throws UserMapperException {
        String primaryEmail = getPrimaryEmail(clerkUser);

        if (clerkUser.getId() == null || primaryEmail == null) {
            throw new UserMapperException("Missing required user fields in webhook payload");
        }

        return User.builder()
                .id(clerkUser.getId())
                .firstName(clerkUser.getFirstName())
                .lastName(clerkUser.getLastName())
                .email(getPrimaryEmail(clerkUser))
                .username(clerkUser.getUsername())
                .build();
    }

    private String getPrimaryEmail(ClerkUserData clerkUser) {
        if (clerkUser.getPrimaryEmailAddressID() == null || clerkUser.getEmailAddresses() == null) {
            return null;
        }
        String primaryEmailId = clerkUser.getPrimaryEmailAddressID();
        for (ClerkEmailAddress email : clerkUser.getEmailAddresses()) {
            if (email.getId() != null && primaryEmailId.equals(email.getId())) {
                return email.getEmailAddress() != null ? email.getEmailAddress() : null;
            }
        }
        return null;
    }
}
