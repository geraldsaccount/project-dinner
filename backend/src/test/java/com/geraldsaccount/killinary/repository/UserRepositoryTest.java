package com.geraldsaccount.killinary.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.model.User;

@DataJpaTest
@ActiveProfiles("test")
@SuppressWarnings("unused")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();

        User testUser = User.builder()
                .id("TEST")
                .email("test@example.com")
                .username("testuser")
                .firstName("test")
                .lastName("user")
                .build();
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    void shouldSaveAndFindUser() {
        User newUser = User.builder()
                .id("U1")
                .email("john@example.com")
                .username("john.doe")
                .firstName("john")
                .lastName("doe")
                .build();

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("john.doe");

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("john.doe");
    }

    @Test
    void shouldFindUserByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void shouldCheckIfUsernameExists() {
        boolean exists = userRepository.existsByUsername("testuser");
        assertThat(exists).isTrue();

        boolean notExists = userRepository.existsByUsername("anotheruser");
        assertThat(notExists).isFalse();
    }

    // @Test
    // @Sql("/data.sql")
    // void shouldLoadDataFromSqlScript() {
    // assertThat(userRepository.count()).isEqualTo(2);
    // Optional<User> userFromSql = userRepository.findByUsername("sqluser");
    // assertThat(userFromSql).isPresent();
    // }
}