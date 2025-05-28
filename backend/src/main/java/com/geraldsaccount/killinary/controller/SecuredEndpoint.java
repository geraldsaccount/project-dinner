package com.geraldsaccount.killinary.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication; // Core Spring Security authentication object
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Annotation to inject the JWT directly
import org.springframework.security.oauth2.jwt.Jwt; // Represents the decoded JWT
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class SecuredEndpoint {
    @GetMapping("/user")
    public Map<String, String> getUserData(Authentication authentication, @AuthenticationPrincipal Jwt jwt) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is protected data. You are authenticated!");

        if (authentication != null) {
            response.put("authenticatedUser", authentication.getName());
        }

        if (jwt != null) {
            response.put("userId", jwt.getSubject());
            response.put("email", jwt.getClaimAsString("email"));
            response.put("username", jwt.getClaimAsString("username"));
            response.put("fullname", jwt.getClaimAsString("fullname"));

        }
        return response;
    }
}
