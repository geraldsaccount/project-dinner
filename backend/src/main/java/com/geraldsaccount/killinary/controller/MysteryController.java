package com.geraldsaccount.killinary.controller;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.service.MysteryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class MysteryController {

    private final MysteryService service;

    @GetMapping()
    public Set<StoryForCreationDto> getStorySummaries() {
        return service.getMysterySummaries();
    }

    @PostMapping()
    public void createMystery(Authentication auth, @RequestBody CreateMysteryDto input) {
        service.createMystery(input);
    }
}
