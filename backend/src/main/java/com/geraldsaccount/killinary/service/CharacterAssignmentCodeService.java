package com.geraldsaccount.killinary.service;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;

@Service
public class CharacterAssignmentCodeService extends UniqueCodeService<CharacterAssignment> {

    public CharacterAssignmentCodeService(CharacterAssignmentRepository repo) {
        super(8, repo);
    }

}
