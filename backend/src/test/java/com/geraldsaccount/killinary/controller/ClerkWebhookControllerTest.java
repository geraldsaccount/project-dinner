package com.geraldsaccount.killinary.controller;

import java.net.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.exceptions.ClerkWebhookException;
import com.geraldsaccount.killinary.exceptions.UserMapperException;
import com.geraldsaccount.killinary.exceptions.UserNotFoundException;
import com.geraldsaccount.killinary.service.ClerkWebhookService;
import com.svix.exceptions.WebhookVerificationException;

import jakarta.servlet.http.HttpServletRequest;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class ClerkWebhookControllerTest {

    private ClerkWebhookService service;
    private ClerkWebhookController controller;

    @BeforeEach
    void setUp() {
        service = mock(ClerkWebhookService.class);
        controller = new ClerkWebhookController(service);
    }

    @Test
    void clerkUserWebhook_shouldCallServiceWithCorrectHeaders() throws Exception {
        String svixId = "id123";
        String svixTimestamp = "timestamp";
        String svixSignature = "signature";
        HttpServletRequest request = mock(HttpServletRequest.class);

        controller.clerkUserWebhook(
                svixId, svixTimestamp, svixSignature, request);

        ArgumentCaptor<HttpHeaders> headersCaptor = ArgumentCaptor.forClass(HttpHeaders.class);
        verify(service).handleUserWebhook(eq(request), headersCaptor.capture());

        HttpHeaders headers = headersCaptor.getValue();
        assertThat(headers.firstValue("svix-id"))
                .hasValue(svixId);
        assertThat(headers.firstValue("svix-timestamp"))
                .hasValue(svixTimestamp);
        assertThat(headers.firstValue("svix-signature"))
                .hasValue(svixSignature);
    }

    @Test
    void clerkUserWebhook_shouldPropagateClerkWebhookException() throws Exception {
        String svixId = "id";
        String svixTimestamp = "ts";
        String svixSignature = "sig";
        HttpServletRequest request = mock(HttpServletRequest.class);

        doThrow(new ClerkWebhookException("error")).when(service)
                .handleUserWebhook(any(), any());

        assertThatThrownBy(() -> controller.clerkUserWebhook(svixId, svixTimestamp, svixSignature, request))
                .isInstanceOf(ClerkWebhookException.class);
    }

    @Test
    void clerkUserWebhook_shouldPropagateWebhookVerificationException() throws Exception {
        String svixId = "id";
        String svixTimestamp = "ts";
        String svixSignature = "sig";
        HttpServletRequest request = mock(HttpServletRequest.class);

        doThrow(new WebhookVerificationException("error")).when(service)
                .handleUserWebhook(any(), any());

        assertThatThrownBy(() -> controller.clerkUserWebhook(svixId, svixTimestamp, svixSignature, request))
                .isInstanceOf(WebhookVerificationException.class);
    }

    @Test
    void clerkUserWebhook_shouldPropagateUserMapperException() throws Exception {
        String svixId = "id";
        String svixTimestamp = "ts";
        String svixSignature = "sig";
        HttpServletRequest request = mock(HttpServletRequest.class);

        doThrow(new UserMapperException("error")).when(service)
                .handleUserWebhook(any(), any());

        assertThatThrownBy(() -> controller.clerkUserWebhook(svixId, svixTimestamp, svixSignature, request))
                .isInstanceOf(UserMapperException.class);
    }

    @Test
    void clerkUserWebhook_shouldPropagateUserNotFoundException() throws Exception {
        String svixId = "id";
        String svixTimestamp = "ts";
        String svixSignature = "sig";
        HttpServletRequest request = mock(HttpServletRequest.class);

        doThrow(new UserNotFoundException("error")).when(service)
                .handleUserWebhook(any(), any());

        assertThatThrownBy(() -> controller.clerkUserWebhook(svixId, svixTimestamp, svixSignature, request))
                .isInstanceOf(UserNotFoundException.class);
    }
}