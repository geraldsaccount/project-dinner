package com.geraldsaccount.killinary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataSourceLogger {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void logDataSourceUrl() {
        String url = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        System.out.println("=== Connected to DB: " + url + " ===");
    }
}