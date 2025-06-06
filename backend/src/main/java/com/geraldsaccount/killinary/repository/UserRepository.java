package com.geraldsaccount.killinary.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geraldsaccount.killinary.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByName(String username);

    Optional<User> findByOauthId(String oauthId);

    Optional<User> findByEmail(String email);

    boolean existsByName(String username);

}
