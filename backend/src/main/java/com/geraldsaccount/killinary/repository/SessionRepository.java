package com.geraldsaccount.killinary.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.geraldsaccount.killinary.model.Session;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    @Query("SELECT s FROM Session s JOIN s.participants u WHERE u.oauthId = :oauthId")
    List<Session> findAllByUserId(String oauthId);
}
