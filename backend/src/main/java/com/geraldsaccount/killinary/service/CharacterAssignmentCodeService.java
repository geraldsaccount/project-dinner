package com.geraldsaccount.killinary.service;

import com.geraldsaccount.killinary.model.CharacterAssignment;
import com.geraldsaccount.killinary.repository.CharacterAssignmentRepository;

public class CharacterAssignmentCodeService extends UniqueCodeService<CharacterAssignment> {

    public CharacterAssignmentCodeService(CharacterAssignmentRepository repo) {
        super(8, repo);
    }

}
