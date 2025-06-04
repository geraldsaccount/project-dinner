package com.geraldsaccount.killinary.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.model.dto.output.StorySummaryDTO;
import com.geraldsaccount.killinary.service.StoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService service;

    @GetMapping()
    public Set<StorySummaryDTO> getStorySummaries() {
        return service.getStorySummaries();
    }
}
