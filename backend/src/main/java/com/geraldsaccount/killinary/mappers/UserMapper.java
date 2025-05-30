package com.geraldsaccount.killinary.mappers;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.model.User;

@Component
public class UserMapper {

    public User withUpdatedUserData(User source, User updated) {
        return source.withFirstName(updated.getFirstName())
                .withLastName(updated.getLastName())
                .withUsername(updated.getUsername())
                .withEmail(updated.getEmail());
    }

    public User fromJsonNode(JsonNode data) throws UserMapperException {
        String id = data.hasNonNull("id") ? data.get("id").asText() : null;
        String firstName = data.hasNonNull("first_name") ? data.get("first_name").asText() : null;
        String lastName = data.hasNonNull("last_name") ? data.get("last_name").asText() : null;
        String primaryEmail = getPrimaryEmail(data);

        String username = data.hasNonNull("username") ? data.get("username").asText() : null;

        if (id == null || primaryEmail == null) {
            throw new UserMapperException("Missing required user fields in webhook payload");
        }
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(primaryEmail)
                .username(username)
                .build();
    }

    private String getPrimaryEmail(JsonNode data) {
        if (!data.hasNonNull("primary_email_address_id") || !data.hasNonNull("email_addresses")) {
            return null;
        }
        String primaryEmailId = data.get("primary_email_address_id").asText();
        for (JsonNode emailNode : data.get("email_addresses")) {
            if (emailNode.hasNonNull("id") && primaryEmailId.equals(emailNode.get("id").asText())) {
                return emailNode.hasNonNull("email_address") ? emailNode.get("email_address").asText() : null;
            }
        }
        return null;
    }
}
