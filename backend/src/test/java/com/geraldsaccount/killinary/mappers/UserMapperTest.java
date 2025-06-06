package com.geraldsaccount.killinary.mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkEmailAddress;
import com.geraldsaccount.killinary.model.dto.clerk.OAuthUserData;

@ActiveProfiles("test")
class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void withUpdatedClerkUserData_shouldUpdateFields() {
        User source = User.builder()
                .oauthId("U1")
                .name("johndoe")
                .email("john@example.com")
                .build();

        User updated = User.builder()
                .name("janesmith")
                .email("jane@example.com")
                .build();

        User result = userMapper.withUpdatedClerkUserData(source, updated);

        assertThat(result.getName()).isEqualTo("janesmith");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        assertThat(result.getOauthId()).isEqualTo("U1");
    }

    @Test
    void fromClerkUser_shouldMapFields_withValidClerkUser() throws UserMapperException {
        ClerkEmailAddress email = ClerkEmailAddress.builder()
                .id("E1")
                .emailAddress("alice@wonderland.com")
                .build();

        OAuthUserData clerkUser = OAuthUserData.builder()
                .id("U1")
                .name("alicew")
                .primaryEmailAddressID(email.getId())
                .emailAddresses(new ClerkEmailAddress[] { email })
                .build();

        User user = userMapper.fromClerkUser(clerkUser);

        assertThat(user.getOauthId()).isEqualTo(clerkUser.getId());
        assertThat(user.getName()).isEqualTo(clerkUser.getName());
        assertThat(user.getEmail()).isEqualTo(email.getEmailAddress());
    }

    @Test
    void fromClerkUser_shouldThrowException_whenIdIsNull() {
        OAuthUserData clerkUser = OAuthUserData.builder()
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
        OAuthUserData clerkUser = OAuthUserData.builder()
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

        OAuthUserData clerkUser = OAuthUserData.builder()
                .id("U1")
                .primaryEmailAddressID("E3")
                .emailAddresses(new ClerkEmailAddress[] { email1, email2 }).build();

        assertThatThrownBy(() -> userMapper.fromClerkUser(clerkUser))
                .isInstanceOf(UserMapperException.class);
    }

    @Test
    void getPrimaryEmail_shouldReturnNullIfPrimaryEmailIdIsNull() throws Exception {
        OAuthUserData clerkUser = OAuthUserData.builder()
                .id("userId")
                .primaryEmailAddressID(null)
                .emailAddresses(null)
                .build();

        assertThatThrownBy(() -> userMapper.fromClerkUser(clerkUser))
                .isInstanceOf(UserMapperException.class);
    }

    @Test
    void asDTO_shouldMapFieldsCorrectly() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("bob")
                .avatarUrl("http://avatar.com/bob.png")
                .build();

        var dto = userMapper.asDTO(user);

        assertThat(dto.id()).isEqualTo(user.getId());
        assertThat(dto.name()).isEqualTo("bob");
        assertThat(dto.avatarUrl()).isEqualTo("http://avatar.com/bob.png");
    }

    @Test
    void asDTO_shouldHandleNullAvatarUrl() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("eve")
                .avatarUrl(null)
                .build();

        var dto = userMapper.asDTO(user);

        assertThat(dto.id()).isEqualTo(user.getId());
        assertThat(dto.name()).isEqualTo("eve");
        assertThat(dto.avatarUrl()).isNull();
    }
}