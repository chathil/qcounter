package com.proximity.labs.qcounter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.advice.AuthControllerAdvice;
import com.proximity.labs.qcounter.data.dto.request.SigninRequest;
import com.proximity.labs.qcounter.data.dto.request.SignupRequest;
import com.proximity.labs.qcounter.data.dto.request.TokenRefreshRequest;
import com.proximity.labs.qcounter.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class AuthControllerTest {

    private MockMvc mvc;

    @Autowired
    AuthService authService;

    @Autowired
    AuthController authController;


    @Autowired
    AuthControllerAdvice authControllerAdvice;


    @Test
    public void whenSignIn_thenReturnSuccessJson() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(authController).build();
        SigninRequest request = new SigninRequest();
        request.setDeviceToken("dev");
        request.setEmail("chathil98@gmail.com");
        request.setIpAddress("192.168.1.1");
        request.setPassword("password");
        mvc.perform(get("/auth/signin")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("chathil98@gmail.com"));
    }

    @Test
    public void whenSignIn_thenReturnNoRecordErrorJson() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(authControllerAdvice).build();
        SigninRequest request = new SigninRequest();
        request.setDeviceToken("dev");
        request.setEmail("chathil@gmail.com");
        request.setIpAddress("192.168.1.1");
        request.setPassword("password");
        mvc.perform(get("/auth/signin")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("Bad credentials"));
    }

    @Test
    public void whenSignUp_thenReturnEmailAlreadyUsedErrorJson() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(authControllerAdvice).build();
        SignupRequest request = new SignupRequest();
        request.setDeviceToken("dev11");
        request.setEmail("chathil98@gmail.com");
        request.setIpAddress("192.168.1.1");
        request.setPassword("password");
        request.setName("Abdul Chathil II");
        mvc.perform(post("/auth/signup")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isImUsed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(String.format("Email already in use with Address : '%s'", request.getEmail())));
    }

    @Test
    public void whenSignUp_thenReturnSuccessJson() {
//        TODO write this test. the last i wrote it got LazyInitializationException, but it work with manual test.
//        ### Signup Ihza
//        POST https://qcounter.herokuapp.com/auth/signup
//        Content-Type: application/json
//
//        {
//            "device_token": "this is a uswer devive toksen sss23345ss",
//                "ip_address": "192.200.020.210",
//                "name": "Ihza SS",
//                "email": "imhza@gmail.com",
//                "password": "password"
//        }
    }

    @Test
    public void whenRefreshToken_thenReturnSuccessJson() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(authControllerAdvice).build();

        TokenRefreshRequest refreshRequest = new TokenRefreshRequest("e765a49f-cdab-4db0-bf7e-e8faea9f74421");

        mvc.perform(get("/auth/refresh")
                .content(new ObjectMapper().writeValueAsString(refreshRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.refresh_token").value("e765a49f-cdab-4db0-bf7e-e8faea9f74421"));
    }

    @Test
    public void whenRefreshToken_thenReturn417ErrorJson() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(authControllerAdvice).build();

        TokenRefreshRequest refreshRequest = new TokenRefreshRequest("e765a4f-cdab-4db0-bf7e-e8faea9f74421");

        mvc.perform(get("/auth/refresh")
                .content(new ObjectMapper().writeValueAsString(refreshRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(String.format("Couldn't refresh token for [e765a4f-cdab-4db0-bf7e-e8faea9f74421]: [Unexpected error during token refresh. Please logout and login again.])", refreshRequest.getRefreshToken())));
    }


}
