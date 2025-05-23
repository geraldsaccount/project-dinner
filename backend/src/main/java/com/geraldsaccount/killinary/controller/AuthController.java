package com.geraldsaccount.killinary.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getMe(@AuthenticationPrincipal OAuth2User user) {
        Object loginAttr = user.getAttribute("login");
        String login = loginAttr != null ? loginAttr.toString() : "";
        // Spring will automatically convert this Map to JSON: {"login":
        // "the_login_value"}
        return Collections.singletonMap("login", login);
    }
}
