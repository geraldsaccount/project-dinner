package com.geraldsaccount.killinary.controller;

import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.geraldsaccount.killinary.exceptions.ClerkWebhookException;
import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.service.ClerkWebhookService;
import com.svix.exceptions.WebhookVerificationException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/webhooks/clerk")
public class ClerkWebhookController {
    private final ClerkWebhookService service;

    public ClerkWebhookController(ClerkWebhookService service) {
        this.service = service;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void clerkUserWebhook(
            @RequestHeader("svix-id") String svixId,
            @RequestHeader("svix-timestamp") String svixTimestamp,
            @RequestHeader("svix-signature") String svixSignature,
            HttpServletRequest request) throws JsonMappingException, JsonProcessingException, ClerkWebhookException,
            WebhookVerificationException, UserMapperException, UserNotFoundException {
        // inconsequential change
        HashMap<String, List<String>> headerMap = new HashMap<>();
        headerMap.put("svix-id", Arrays.asList(svixId));
        headerMap.put("svix-timestamp", Arrays.asList(svixTimestamp));
        headerMap.put("svix-signature", Arrays.asList(svixSignature));

        HttpHeaders headers = HttpHeaders.of(headerMap, (k, v) -> true);
        service.handleUserWebhook(request, headers);
    }
}