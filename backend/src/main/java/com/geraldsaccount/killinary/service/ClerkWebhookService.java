package com.geraldsaccount.killinary.service;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.exceptions.ClerkWebhookException;
import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkUserPayload;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ClerkWebhookService {
    private final UserService userService;

    private ObjectMapper objectMapper;

    private UserMapper userMapper;

    @Value("${clerk.webhook.secret}")
    private String webhookSecret;

    private Webhook webhook;

    @PostConstruct
    public void setWebhook() {
        if (this.webhook == null) {
            this.webhook = new Webhook(webhookSecret);
        }
    }

    @Autowired
    public ClerkWebhookService(UserService userService, UserMapper userMapper, ObjectMapper objectMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
    }

    public ClerkWebhookService(UserService userService, UserMapper userMapper, ObjectMapper objectMapper,
            Webhook webhook) {
        this(userService, userMapper, objectMapper);
        this.webhook = webhook;
    }

    public void handleUserWebhook(HttpServletRequest request, HttpHeaders headers)
            throws ClerkWebhookException, WebhookVerificationException, JsonProcessingException, UserNotFoundException,
            UserMapperException {
        String payload = verifyWebhook(headers, request);

        ClerkUserPayload userPayload = objectMapper.readValue(payload, ClerkUserPayload.class);

        switch (userPayload.getType()) {
            case "user.created" -> userService.createUserData(userMapper.fromClerkUser(userPayload.getData()));
            case "user.updated" -> userService.updateUserData(userMapper.fromClerkUser(userPayload.getData()));
            case "user.deleted" -> userService.deleteUser(userPayload.getData().getId());
            default -> {
            }
        }
    }

    public String verifyWebhook(HttpHeaders headers, HttpServletRequest request)
            throws ClerkWebhookException, WebhookVerificationException {
        String payload;
        try {
            payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new ClerkWebhookException("Error reading webhook payload", e);
        }

        if (payload == null || payload.isEmpty()) {
            throw new ClerkWebhookException("Webhook payload is empty");
        }

        webhook.verify(payload, headers);
        return payload;
    }
}
