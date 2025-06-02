package com.geraldsaccount.killinary.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unused")
@ActiveProfiles("test")
class SecuredEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserData_deniesAccess_whenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/protected/user"))
                .andExpect(status().isUnauthorized()); // expect 401
    }

    @Test
    void getUserData_allowsAccess_withValidClerkJwt() throws Exception {
        Map<String, Object> jwtClaims = new HashMap<>();
        jwtClaims.put(JwtClaimNames.SUB, "user_2U5aLg5Vw8xxxxxxxxxx");
        jwtClaims.put("userId", "user_2U5aLg5Vw8xxxxxxxxxx");
        jwtClaims.put("email", "test@example.com");
        jwtClaims.put("username", "testuser123");
        jwtClaims.put("sid", "sess_xxxxxxxxxxxxxxxxx");

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(3600); // valid for 1 hour

        Jwt mockJwt = Jwt.withTokenValue("mock-jwt-token-string") // placeholder
                .header("alg", "RS256") // algorithm used by Clerk
                .headers(h -> h.putAll(headers))
                .claims(c -> c.putAll(jwtClaims))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .jti("jti_xxxxxxxxxxxxxxxxx") // jwt id
                .build();

        String responseJson = mockMvc.perform(get("/api/protected/user")
                .with(jwt().jwt(mockJwt)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        @SuppressWarnings("unchecked")
        Map<String, String> response = objectMapper.readValue(responseJson, Map.class);

        assertNotNull(response);
        assertEquals("This is protected data. You are authenticated!", response.get("message"));
        assertEquals("user_2U5aLg5Vw8xxxxxxxxxx", response.get("authenticatedUser"));
        assertEquals("user_2U5aLg5Vw8xxxxxxxxxx", response.get("userId"));
        assertEquals("test@example.com", response.get("email"));
        assertEquals("testuser123", response.get("username"));
    }

    @Test
    @WithMockUser
    void getUserData_allowsAccess_withMockUser() throws Exception {
        mockMvc.perform(get("/api/protected/user"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserData_returnsNullEmail_withMissingEmail() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimNames.SUB, "user_no_email");
        claims.put("userId", "user_no_email");
        claims.put("username", "noemailuser");
        // omit "email" claim

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(3600);

        Jwt mockJwt = Jwt.withTokenValue("mock-jwt-no-email")
                .header("alg", "RS256")
                .claims(c -> c.putAll(claims))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();

        String responseJson = mockMvc.perform(get("/api/protected/user")
                .with(jwt().jwt(mockJwt)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        @SuppressWarnings("unchecked")
        Map<String, String> response = objectMapper.readValue(responseJson, Map.class);

        assertNotNull(response);
        assertEquals("This is protected data. You are authenticated!", response.get("message"));
        assertEquals("user_no_email", response.get("userId"));
        assertEquals("noemailuser", response.get("username"));
        assertEquals(null, response.get("email"));
    }
}
