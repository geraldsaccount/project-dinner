package com.geraldsaccount.killinary.service;

import org.springframework.stereotype.Service;

import com.geraldsaccount.killinary.model.Session;
import com.geraldsaccount.killinary.repository.SessionRepository;

@Service
public class SessionCodeService extends UniqueCodeService<Session> {

    public SessionCodeService(SessionRepository repo) {
        super(8, repo);
    }
}
