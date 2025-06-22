package com.geraldsaccount.killinary.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedDinnerDto;
import com.geraldsaccount.killinary.model.dto.output.other.InvitationViewDto;
import com.geraldsaccount.killinary.service.CharacterAssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
public class InviteController {
    private final CharacterAssignmentService assignmentService;

    @PutMapping("{code}")
    public CreatedDinnerDto acceptInvitation(Authentication authentication, @PathVariable String code)
            throws UserNotFoundException, CharacterAssignmentNotFoundException, AccessDeniedException {
        return assignmentService.acceptInvitation(authentication.getName(), code);
    }

    @GetMapping("{code}")
    public InvitationViewDto getInvitation(Authentication authentication, @PathVariable String code)
            throws UserNotFoundException, CharacterAssignmentNotFoundException {
        return assignmentService.getInvitation(authentication, code);
    }

}
