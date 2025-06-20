package com.geraldsaccount.killinary.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geraldsaccount.killinary.model.mystery.PlayerConfig;

public interface PlayerConfigRepository extends JpaRepository<PlayerConfig, UUID> {

}
