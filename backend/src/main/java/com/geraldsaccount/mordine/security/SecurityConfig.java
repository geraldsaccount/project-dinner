package com.geraldsaccount.mordine.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)// enable when ready to authenticate people or something i dont know
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/api/auth").authenticated()
                .requestMatchers("/api/profile").authenticated()
                .anyRequest().permitAll())
        .oauth2Login(o -> o.defaultSuccessUrl("http://localhost:5173"));

    return http.build();
  }
}
