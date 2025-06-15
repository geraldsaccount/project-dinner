package com.geraldsaccount.killinary.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geraldsaccount.killinary.model.mystery.Mystery;

public interface MysteryRepository extends JpaRepository<Mystery, UUID> {

}
