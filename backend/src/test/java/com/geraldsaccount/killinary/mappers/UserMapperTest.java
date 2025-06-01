package com.geraldsaccount.killinary.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkEmailAddress;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkUserData;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void withUpdatedClerkUserData_shouldUpdateFields() {
        User source = User.builder()
                .clerkId("U1")
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john@example.com")
                .build();

        User updated = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("janesmith")
                .email("jane@example.com")
                .build();

        User result = userMapper.withUpdatedClerkUserData(source, updated);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getUsername()).isEqualTo("janesmith");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        assertThat(result.getClerkId()).isEqualTo("U1");
    }

    @Test
    void fromClerkUser_shouldMapFields_withValidClerkUser() throws UserMapperException {
        ClerkEmailAddress email = ClerkEmailAddress.builder()
                .id("E1")
                .emailAddress("alice@wonderland.com")
                .build();

        ClerkUserData clerkUser = ClerkUserData.builder()
                .id("U1")
                .firstName("Alice")
                .lastName("Wonderland")
                .username("alicew")
                .primaryEmailAddressID(email.getId())
                .emailAddresses(new ClerkEmailAddress[] { email })
                .build();

        User user = userMapper.fromClerkUser(clerkUser);

        assertThat(user.getClerkId()).isEqualTo(clerkUser.getId());
        assertThat(user.getFirstName()).isEqualTo(clerkUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(clerkUser.getLastName());
        assertThat(user.getUsername()).isEqualTo(clerkUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(email.getEmailAddress());
    }

    @Test
    void fromClerkUser_shouldThrowException_whenIdIsNull() {
        ClerkUserData clerkUser = ClerkUserData.builder()
                .id(null)
                .primaryEmailAddressID("E1")
                .emailAddresses(new ClerkEmailAddress[] {})
                .build();

        assertThatThrownBy(() -> userMapper.fromClerkUser(clerkUser))
                .isInstanceOf(UserMapperException.class)
                .hasMessageContaining("Missing required user fields");
    }

    @Test
    void fromClerkUser_shouldThrowException_whenPrimaryEmailIsNull() {
        ClerkUserData clerkUser = ClerkUserData.builder()
                .id("U1")
                .primaryEmailAddressID(null)
                .emailAddresses(new ClerkEmailAddress[] {})
                .build();

        assertThatThrownBy(() -> userMapper.fromClerkUser(clerkUser))
                .isInstanceOf(UserMapperException.class)
                .hasMessageContaining("Missing required user fields");
    }

    @Test
    void getPrimaryEmail_shouldReturnNull_ifNoIdMatches() throws Exception {
        ClerkEmailAddress email1 = ClerkEmailAddress.builder()
                .id("E1")
                .emailAddress("alice@wonderland.com")
                .build();
        ClerkEmailAddress email2 = ClerkEmailAddress.builder()
                .id("E2")
                .emailAddress("madhatter@wonderland.com")
                .build();

        ClerkUserData clerkUser = ClerkUserData.builder()
                .id("U1")
                .primaryEmailAddressID("E3")
                .emailAddresses(new ClerkEmailAddress[] { email1, email2 }).build();

        assertThatThrownBy(() -> userMapper.fromClerkUser(clerkUser))
                .isInstanceOf(UserMapperException.class);
    }

    @Test
    void getPrimaryEmail_shouldReturnNullIfPrimaryEmailIdIsNull() throws Exception {
        ClerkUserData clerkUser = ClerkUserData.builder()
                .id("userId")
                .primaryEmailAddressID(null)
                .emailAddresses(null)
                .build();

        assertThatThrownBy(() -> userMapper.fromClerkUser(clerkUser))
                .isInstanceOf(UserMapperException.class);
    }
}