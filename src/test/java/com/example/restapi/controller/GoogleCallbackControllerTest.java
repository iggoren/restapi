package com.example.restapi.controller;

import com.example.restapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class GoogleCallbackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Test
    void getGoogleAuthUrl() {
    }

    @Test
    void handleGoogleCallback() {
    }

    @Test
    void getCalendars() throws Exception {

        HttpSession session = mockMvc.perform(get("/api/oauth/google/auth-url"))
                .andReturn()
                .getRequest()
                .getSession();

        mockMvc.perform(get("/api/oauth/callback/google")
                        .session((MockHttpSession) session))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/api/oauth/calendars")
                        .session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void saveCalendarId() {
    }

    @Test
    void postEventToCalendar() {
    }
}