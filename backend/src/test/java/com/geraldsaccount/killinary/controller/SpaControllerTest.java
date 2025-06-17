package com.geraldsaccount.killinary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.geraldsaccount.killinary.KillinaryApplication;

@SpringBootTest(classes = KillinaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("unused")
class SpaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void forward_nonApiNonStaticPath_forwardsToRoot() throws Exception {
        mockMvc.perform(get("/some-client-route"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/"));
    }

    @Test
    void forward_apiPath_doesNotForward() throws Exception {
        mockMvc.perform(get("/api/something"))
                .andExpect(status().isNotFound()); // Should not be handled by SpaController
    }

    @Test
    void forward_staticFilePath_doesNotForward() throws Exception {
        mockMvc.perform(get("/main.js"))
                .andExpect(status().isNotFound()); // Should not be handled by SpaController
    }
}
