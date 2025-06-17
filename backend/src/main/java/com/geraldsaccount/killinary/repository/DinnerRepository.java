package com.geraldsaccount.killinary.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.geraldsaccount.killinary.model.dinner.Dinner;

public interface DinnerRepository extends JpaRepository<Dinner, UUID> {
    @Query("SELECT d FROM Dinner d JOIN d.participants u WHERE u.oauthId = :oauthId")
    List<Dinner> findAllByUserId(String oauthId);
}
