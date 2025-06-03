package com.geraldsaccount.killinary.controller;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.exceptions.NotAllowedException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.model.dto.input.SessionCreationDTO;
import com.geraldsaccount.killinary.model.dto.output.NewSessionDTO;
import com.geraldsaccount.killinary.model.dto.output.SessionSummaryDTO;
import com.geraldsaccount.killinary.service.SessionService;

@RestController
@RequestMapping("/api/sessions")
public class SessionControler {

    private final SessionService sessionService;

    public SessionControler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping()
    public Set<SessionSummaryDTO> getSessionsForUser(Authentication authentication) {
        return sessionService.getSessionSummariesFrom(authentication.getName());
    }

    @PostMapping()
    public NewSessionDTO createNewSession(Authentication authentication, @RequestBody SessionCreationDTO creationDTO)
            throws UserNotFoundException, StoryNotFoundException, StoryConfigurationNotFoundException,
            NotAllowedException {
        return sessionService.createSession(authentication.getName(), creationDTO);
    }
}
