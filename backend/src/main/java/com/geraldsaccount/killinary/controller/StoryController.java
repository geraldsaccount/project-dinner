package com.geraldsaccount.killinary.controller;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.model.dto.input.CreateStoryDto;
import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.service.StoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService service;

    @GetMapping()
    public Set<StoryForCreationDto> getStorySummaries() {
        return service.getStorySummaries();
    }

    @PostMapping()
    public void createStory(Authentication auth, @RequestBody CreateStoryDto input) {
        service.createStory(input);
    }
}
