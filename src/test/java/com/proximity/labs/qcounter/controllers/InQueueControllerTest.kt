package com.proximity.labs.qcounter.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.proximity.labs.qcounter.advice.AuthControllerAdvice
import com.proximity.labs.qcounter.data.dto.request.InQueueRequest
import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest
import com.proximity.labs.qcounter.data.dto.request.RemoveFromInQueueRequest
import com.proximity.labs.qcounter.data.models.user.User
import com.proximity.labs.qcounter.service.InQueueService
import com.proximity.labs.qcounter.service.QueueService
import com.proximity.labs.qcounter.utils.FakeDataDummy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.MethodParameter
import org.springframework.data.util.Pair
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*


@SpringBootTest
@ExtendWith(MockitoExtension::class)
internal class InQueueControllerTest {

    @Autowired
    lateinit var inQueueController: InQueueController

    @Autowired
    lateinit var authControllerAdvice: AuthControllerAdvice

    @MockBean
    lateinit var inQueueService: InQueueService

    @MockBean
    lateinit var queueService: QueueService

    private lateinit var mockMvc: MockMvc

    private fun putAuthenticationPrincipal(idx: Int): HandlerMethodArgumentResolver? {
        return object : HandlerMethodArgumentResolver {
            override fun supportsParameter(parameter: MethodParameter): Boolean {
                return parameter.parameterType.isAssignableFrom(User::class.java)
            }

            override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer,
                                         webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory): Any {
                return FakeDataDummy.user()[idx]
            }
        }
    }


    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal(0)).setControllerAdvice(authControllerAdvice).build()
        `when`(queueService.findFirstByClientGeneratedId(any(String::class.java))).thenReturn(Optional.of(FakeDataDummy.savedQueue()))
        `when`(inQueueService.findByQueueId(any(Long::class.java))).thenReturn(FakeDataDummy.inQueues())
        `when`(inQueueService.joinQueueAndPersist(any(User::class.java), any(JoinQueueRequest::class.java))).thenReturn(Optional.of(Pair.of(FakeDataDummy.savedQueue(), FakeDataDummy.inQueue())))
    }

    @Test
    fun whenInQueues_thenReturnSucessJson() {
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal(0)).setControllerAdvice(authControllerAdvice).build()
        val inQRequest = InQueueRequest("clientGeneratedId")
        mockMvc
                .perform(get("/in_queue")
                        .content(ObjectMapper().writeValueAsString(inQRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun whenInQueues_thenReturnOwnerError() {
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal(2)).setControllerAdvice(authControllerAdvice).build()
        val inQRequest = InQueueRequest("clientGeneratedId")
        mockMvc
                .perform(get("/in_queue")
                        .content(ObjectMapper().writeValueAsString(inQRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("Queue with id ${inQRequest.queueId} doesn't belongs to ${FakeDataDummy.user()[2].name}"))
    }

    @Test
    fun whenInQueues_thenReturnDoesntExistError() {
        `when`(queueService.findFirstByClientGeneratedId(any(String::class.java))).thenReturn(Optional.empty())
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal(2)).setControllerAdvice(authControllerAdvice).build()
        val inQRequest = InQueueRequest("clientGeneratedId")
        mockMvc
                .perform(get("/in_queue")
                        .content(ObjectMapper().writeValueAsString(inQRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("Queue not found with queue_id : '${inQRequest.queueId}'"))
    }

    @Test
    fun whenAddToQueue_thenReturnSuccessJson() {
        `when`(inQueueService.addToQueueAndPersist(any(User::class.java), any(JoinQueueRequest::class.java))).thenReturn(Optional.of(Pair.of(FakeDataDummy.savedQueue(), FakeDataDummy.inQueue())))
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal(0)).setControllerAdvice(authControllerAdvice).build()
        println(ObjectMapper().writeValueAsString(FakeDataDummy.joinQueueRequests()[2]))
        mockMvc.perform(post("/in_queue")
                .content(ObjectMapper().writeValueAsString(FakeDataDummy.joinQueueRequests()[2]))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    }

    @Test
    fun whenAddToQueue_thenReturnDoesntExistError() {
        `when`(inQueueService.addToQueueAndPersist(any(User::class.java), any(JoinQueueRequest::class.java))).thenReturn(Optional.empty())
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal(0)).setControllerAdvice(authControllerAdvice).build()
        mockMvc.perform(post("/in_queue")
                .content(ObjectMapper().writeValueAsString(FakeDataDummy.joinQueueRequests()[2]))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("Queue not found with queue_id : '${FakeDataDummy.joinQueueRequests()[2].queueId}'"))
    }

    @Test
    fun whenDeleteFromQueue_thenReturnSuccessJson() {
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal(0)).setControllerAdvice(authControllerAdvice).build()
        val request = RemoveFromInQueueRequest(1L, FakeDataDummy.savedQueue().clientGeneratedId);
        mockMvc.perform(delete("/in_queue")
                .content(ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("User with id ${request.id} removed"))
    }
}