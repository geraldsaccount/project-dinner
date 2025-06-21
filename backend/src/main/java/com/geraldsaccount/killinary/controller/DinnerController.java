package com.geraldsaccount.killinary.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.exceptions.AccessDeniedException;
import com.geraldsaccount.killinary.exceptions.CharacterAssignmentNotFoundException;
import com.geraldsaccount.killinary.exceptions.CharacterNotFoundException;
import com.geraldsaccount.killinary.exceptions.DinnerNotFoundException;
import com.geraldsaccount.killinary.exceptions.MysteryNotFoundException;
import com.geraldsaccount.killinary.exceptions.StoryConfigurationNotFoundException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.model.dto.input.CreateDinnerDto;
import com.geraldsaccount.killinary.model.dto.input.dinner.VoteDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerSummaryDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.DinnerView;
import com.geraldsaccount.killinary.model.dto.output.other.CreatedDinnerDto;
import com.geraldsaccount.killinary.service.DinnerService;

@RestController
@RequestMapping("/api/dinners")
public class DinnerController {

    private final DinnerService dinnerService;

    public DinnerController(DinnerService dinnerService) {
        this.dinnerService = dinnerService;
    }

    @GetMapping()
    public Set<DinnerSummaryDto> getDinnersForUser(Authentication authentication) {
        return dinnerService.getDinnerSummariesFrom(authentication.getName());
    }

    @GetMapping("{id}")
    public DinnerView getDinnerView(Authentication authentication, @PathVariable UUID id)
            throws UserNotFoundException, DinnerNotFoundException, AccessDeniedException,
            CharacterAssignmentNotFoundException {
        return dinnerService.getDinnerView(authentication.getName(), id);
    }

    @PostMapping()
    public CreatedDinnerDto createNewDinner(Authentication authentication, @RequestBody CreateDinnerDto creationDTO)
            throws UserNotFoundException, MysteryNotFoundException, StoryConfigurationNotFoundException,
            AccessDeniedException {
        return dinnerService.createDinner(authentication.getName(), creationDTO);
    }

    @PutMapping("{id}/progress")
    public DinnerView progressDinner(Authentication authentication, @PathVariable UUID id) throws UserNotFoundException,
            DinnerNotFoundException, CharacterAssignmentNotFoundException, AccessDeniedException {
        return dinnerService.progressDinner(authentication.getName(), id);
    }

    @PostMapping("{id}")
    public DinnerView castSuspectVote(Authentication authentication, @PathVariable UUID id, @RequestBody VoteDto vote)
            throws UserNotFoundException, DinnerNotFoundException, CharacterAssignmentNotFoundException,
            AccessDeniedException, CharacterNotFoundException {
        return dinnerService.castVote(authentication.getName(), id, vote);
    }

}
