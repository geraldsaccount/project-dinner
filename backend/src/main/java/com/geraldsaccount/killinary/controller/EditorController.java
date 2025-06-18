package com.geraldsaccount.killinary.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.service.EditorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/editor")
@RequiredArgsConstructor

public class EditorController {
    private final EditorService service;

    @PostMapping()
    public void createMystery(Authentication auth, @RequestBody CreateMysteryDto input) {
        service.createMystery(input);
    }
}
