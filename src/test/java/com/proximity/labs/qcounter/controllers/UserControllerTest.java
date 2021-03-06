package com.proximity.labs.qcounter.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.advice.AuthControllerAdvice;
import com.proximity.labs.qcounter.data.dto.request.SignoutRequest;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.repositories.UserRepository;
import com.proximity.labs.qcounter.security.JwtTokenProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class UserControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    AuthControllerAdvice authControllerAdvice;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private HandlerMethodArgumentResolver putAuthenticationPrincipal(@NotNull String email) {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(@NotNull MethodParameter parameter) {
                return parameter.getParameterType().isAssignableFrom(User.class);
            }

            @Override
            public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return userRepository.findByEmail(email).get();
            }
        };
    }

    @Test
    public void whenLogout_thenReturnSuccessJson() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("mark@gmail.com"))
                .setControllerAdvice(authControllerAdvice)
                .build();

        SignoutRequest signoutRequest = new SignoutRequest("eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ6");
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder.getContext().getAuthentication().getCredentials()).thenReturn(tokenProvider.generateTokenFromUserId(4L));
        mockMvc.perform(get("/user/signout").content(new ObjectMapper().writeValueAsString(signoutRequest)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("Log out successful"));
    }


}
