package com.geraldsaccount.killinary.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geraldsaccount.killinary.exceptions.MysteryCreationException;
import com.geraldsaccount.killinary.model.dto.input.create.CreateMysteryDto;
import com.geraldsaccount.killinary.service.EditorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/editor")
@RequiredArgsConstructor

public class EditorController {
    private final EditorService service;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void createMystery(
            Authentication auth,
            @RequestPart("mystery") CreateMysteryDto mysteryDto,
            @RequestParam(required = false) Map<String, MultipartFile> files) throws MysteryCreationException {
        service.createMystery(mysteryDto, files);
    }
}
