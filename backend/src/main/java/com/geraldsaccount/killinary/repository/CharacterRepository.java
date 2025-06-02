package com.geraldsaccount.killinary.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geraldsaccount.killinary.model.Character;

public interface CharacterRepository extends JpaRepository<Character, UUID> {

}
