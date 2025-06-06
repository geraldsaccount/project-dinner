package com.geraldsaccount.killinary.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.service.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
public class InviteController {
    private final SessionService sessionService;

    @PutMapping("{code}")
    public String acceptInvitation(Authentication authentication, @PathVariable String code) {
        return sessionService.assignToCharacter(authentication.getName(), code);
    }
}
