package com.proximity.labs.qcounter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.advice.AuthControllerAdvice;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.repositories.UserRepository;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class QueueControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    QueueController queueController;
    @Autowired
    AuthControllerAdvice authControllerAdvice;

    private MockMvc mockMvc;

    private HandlerMethodArgumentResolver putAuthenticationPrincipal(String email) {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().isAssignableFrom(User.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return userRepository.findByEmail(email).get();
            }
        };
    }

    @Test
    public void whenCreateQueue_thenReturnLimitReachedErrorJson() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(queueController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("chathil98@gmail.com"))
                .setControllerAdvice(authControllerAdvice)
                .build();
        mockMvc.
                perform(MockMvcRequestBuilders.post("/queue").content(new ObjectMapper().writeValueAsString(FakeDataDummy.queueRequest().get(0))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("You have reached max queue you can create. which is two"));

    }

    @Test
    public void whenCreateQueue_thenReturnGeneralErrorJson() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(queueController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("ihza@gmail.com"))
                .setControllerAdvice(authControllerAdvice)
                .build();
        mockMvc.
                perform(MockMvcRequestBuilders.post("/queue").content(new ObjectMapper().writeValueAsString(FakeDataDummy.badQueueRequest().get(0))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void whenCreateQueue_thenReturnSuccessJson() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(queueController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("yusuf@gmail.com"))
                .build();
        mockMvc.
                perform(MockMvcRequestBuilders.post("/queue").content(new ObjectMapper().writeValueAsString(FakeDataDummy.queueRequest().get(1))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("My Queue2"));
    }

    @Test
    public void whenJoinQueue_thenReturnQueueDoesNotExistErrorJson() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(queueController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("gabrielle@gmail.com"))
                .setControllerAdvice(authControllerAdvice)
                .build();
        mockMvc.
                perform(MockMvcRequestBuilders.post("/queue/join").content(new ObjectMapper().writeValueAsString(FakeDataDummy.joinQueueRequests().get(3))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(String.format("Queue with id %s not exist", FakeDataDummy.joinQueueRequests().get(3).getQueueId())));
    }

    @Test
    public void whenJoinQueue_thenReturnLimitReachedErrorJson() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(queueController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("lakeesha@gmail.com"))
                .setControllerAdvice(authControllerAdvice)
                .build();
        mockMvc.
                perform(MockMvcRequestBuilders.post("/queue/join").content(new ObjectMapper().writeValueAsString(FakeDataDummy.joinQueueRequests().get(0))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("You have reached max queue you can join. which is two"));
    }

    @Test
    public void whenJoinQueue_thenReturnAlreadyJoinErrorJson() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(queueController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("gabrielle@gmail.com"))
                .setControllerAdvice(authControllerAdvice)
                .build();
        mockMvc.
                perform(MockMvcRequestBuilders.post("/queue/join").content(new ObjectMapper().writeValueAsString(FakeDataDummy.joinQueueRequests().get(1))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(String.format("You already joined queue with id %s", FakeDataDummy.joinQueueRequests().get(1).getQueueId())));
    }

    @Test
    public void whenJoinQueue_thenReturnSuccessJson() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(queueController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal("mark@gmail.com"))
                .build();

        mockMvc.
                perform(MockMvcRequestBuilders.post("/queue/join").content(new ObjectMapper().writeValueAsString(FakeDataDummy.joinQueueRequests().get(2))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.queue_name").value("Lorem Queue Chathil"));
    }

}
