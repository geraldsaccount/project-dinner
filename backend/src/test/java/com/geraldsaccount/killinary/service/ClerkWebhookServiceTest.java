package com.geraldsaccount.killinary.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.http.HttpHeaders;
import java.util.Map;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraldsaccount.killinary.exceptions.ClerkWebhookException;
import com.geraldsaccount.killinary.mappers.UserMapper;
import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkUserData;
import com.geraldsaccount.killinary.model.dto.clerk.ClerkUserPayload;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;

import jakarta.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
class ClerkWebhookServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Webhook webhook;

    private ClerkWebhookService webhookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webhookService = new ClerkWebhookService(userService, userMapper, objectMapper, webhook);
    }

    @Test
    void handleUserWebhook_shouldCallCreateUserData_whenUserCreated() throws Exception {
        String payload = "{\"type\":\"user.created\",\"data\":{\"id\":\"123\"}}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(payload)));
        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        ClerkUserPayload userPayload = mock(ClerkUserPayload.class);
        when(userPayload.getType()).thenReturn("user.created");
        ClerkUserData clerkUserData = mock(ClerkUserData.class);
        when(userPayload.getData()).thenReturn(clerkUserData);

        when(objectMapper.readValue(payload, ClerkUserPayload.class)).thenReturn(userPayload);
        when(userMapper.fromClerkUser(clerkUserData)).thenReturn(mock(User.class));

        webhookService.handleUserWebhook(request, headers);

        Mockito.verify(userService).createUserData(any());
    }

    @Test
    void handleUserWebhook_shouldCallUpdateUserData_whenUserUpdated() throws Exception {
        String payload = "{\"type\":\"user.updated\",\"data\":{\"id\":\"123\"}}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(payload)));
        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        ClerkUserPayload userPayload = mock(ClerkUserPayload.class);
        when(userPayload.getType()).thenReturn("user.updated");
        ClerkUserData clerkUserData = mock(ClerkUserData.class);
        when(userPayload.getData()).thenReturn(clerkUserData);

        when(objectMapper.readValue(payload, ClerkUserPayload.class)).thenReturn(userPayload);
        when(userMapper.fromClerkUser(clerkUserData)).thenReturn(mock(User.class));

        webhookService.handleUserWebhook(request, headers);

        Mockito.verify(userService).updateUserData(any());
    }

    @Test
    void handleUserWebhook_shouldCallDeleteUser_whenUserDeleted() throws Exception {
        String payload = "{\"type\":\"user.deleted\",\"data\":{\"id\":\"123\"}}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(payload)));
        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        ClerkUserPayload userPayload = mock(ClerkUserPayload.class);
        when(userPayload.getType()).thenReturn("user.deleted");
        ClerkUserData clerkUserData = mock(ClerkUserData.class);
        when(userPayload.getData()).thenReturn(clerkUserData);
        when(clerkUserData.getId()).thenReturn("U1");

        when(objectMapper.readValue(payload, ClerkUserPayload.class)).thenReturn(userPayload);

        webhookService.handleUserWebhook(request, headers);

        Mockito.verify(userService).deleteUser("U1");
    }

    @Test
    void handleUserWebhook_shouldNotCallAnyUserService_whenTypeIsUnknown() throws Exception {
        String payload = "{\"type\":\"user.unknown\",\"data\":{\"id\":\"123\"}}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(payload)));
        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        ClerkUserPayload userPayload = mock(ClerkUserPayload.class);
        when(userPayload.getType()).thenReturn("user.unknown");
        ClerkUserData clerkUserData = mock(ClerkUserData.class);
        when(userPayload.getData()).thenReturn(clerkUserData);

        when(objectMapper.readValue(payload, ClerkUserPayload.class)).thenReturn(userPayload);

        webhookService.handleUserWebhook(request, headers);

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void handleUserWebhook_shouldThrowException_whenVerifyWebhookThrows() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        when(request.getReader()).thenThrow(new IOException("Failed"));

        assertThatThrownBy(() -> webhookService.handleUserWebhook(request, headers))
                .isInstanceOf(ClerkWebhookException.class)
                .hasMessageContaining("Error reading webhook payload");
    }

    @Test
    void handleUserWebhook_shouldThrowException_whenObjectMapperThrows() throws Exception {
        String payload = "{\"type\":\"user.created\",\"data\":{\"id\":\"123\"}}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(payload)));
        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        when(objectMapper.readValue(payload, ClerkUserPayload.class))
                .thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("fail") {
                });

        assertThatThrownBy(() -> webhookService.handleUserWebhook(request, headers))
                .isInstanceOf(com.fasterxml.jackson.core.JsonProcessingException.class);
    }

    @Test
    void verifyWebhook_shouldReturnPayload_whenValid() throws Exception {
        String payload = "{\"test\":true}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader())
                .thenReturn(new BufferedReader(new StringReader(payload)));

        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);
        String result = webhookService.verifyWebhook(headers, request);
        assertThat(result).isEqualTo(payload);
    }

    @Test
    void verifyWebhook_shouldThrowClerkWebhookException_whenPayloadIsEmpty() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));

        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        Assertions.assertThatThrownBy(() -> webhookService.verifyWebhook(headers, request))
                .isInstanceOf(ClerkWebhookException.class)
                .hasMessageContaining("Webhook payload is empty");
    }

    @Test
    void verifyWebhook_shouldThrowClerkWebhookException_whenIOException() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getReader()).thenThrow(new IOException("IO error"));

        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        Assertions.assertThatThrownBy(() -> webhookService.verifyWebhook(headers, request))
                .isInstanceOf(ClerkWebhookException.class)
                .hasMessageContaining("Error reading webhook payload");
    }

    @Test
    void verifyWebhook_shouldThrowWebhookVerificationException_whenVerificationFails() throws Exception {
        String payload = "{\"test\":true}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(payload)));

        HttpHeaders headers = HttpHeaders.of(Map.of(), (k, v) -> true);

        doThrow(new WebhookVerificationException("Invalid signature"))
                .when(webhook).verify(anyString(), any());

        assertThatThrownBy(() -> webhookService.verifyWebhook(headers, request))
                .isInstanceOf(WebhookVerificationException.class)
                .hasMessageContaining("Invalid signature");
    }
}