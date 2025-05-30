package com.geraldsaccount.killinary.service;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.exceptions.ClerkWebhookException;
import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ClerkWebhookService {
    private final UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Value("${clerk.webhook.secret}")
    private String webhookSecret;

    public ClerkWebhookService(UserService userService) {
        this.userService = userService;
    }

    public void handleUserWebhook(HttpServletRequest request, HttpHeaders headers)
            throws ClerkWebhookException, WebhookVerificationException, JsonMappingException, JsonProcessingException,
            UserMapperException, UserNotFoundException {
        String payload = verifyWebhook(headers, request);

        JsonNode webhookEvent = objectMapper.readTree(payload);
        String eventType = webhookEvent.get("type").asText();
        JsonNode data = webhookEvent.get("data");

        switch (eventType) {
            case "user.created" -> userService.createUserData(userMapper.fromJsonNode(data));
            case "user.updated" -> userService.updateUserData(userMapper.fromJsonNode(data));
            case "user.deleted" -> userService.deleteUser(data.get("id").asText());
            default -> {
            }
        }
    }

    private String verifyWebhook(HttpHeaders headers, HttpServletRequest request)
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

        Webhook webhook = new Webhook(webhookSecret);
        webhook.verify(payload, headers);
        return payload;
    }
}
