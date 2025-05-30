package com.geraldsaccount.killinary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.svix.exceptions.WebhookVerificationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClerkWebhookException.class)
    public ResponseEntity<String> handleClerkWebhookException(ClerkWebhookException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(WebhookVerificationException.class)
    public ResponseEntity<String> handleWebhookVerificationException(WebhookVerificationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
        return ResponseEntity.badRequest().body("JSON processing error: " + ex.getOriginalMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserMapperException.class)
    public ResponseEntity<String> handleUserMapperException(UserMapperException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
