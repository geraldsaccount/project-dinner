package com.geraldsaccount.killinary.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;

public interface CharacterAssignmentRepository
        extends JpaRepository<CharacterAssignment, UUID>, CodedRepository<CharacterAssignment> {
}
