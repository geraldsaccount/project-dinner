package com.geraldsaccount.killinary.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.model.dto.output.other.StoryForCreationDto;
import com.geraldsaccount.killinary.service.MysteryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mysteries")
@RequiredArgsConstructor
public class MysteryController {

    private final MysteryService service;

    @GetMapping()
    public Set<StoryForCreationDto> getMysterySummaries() {
        return service.getMysterySummaries();
    }
}
