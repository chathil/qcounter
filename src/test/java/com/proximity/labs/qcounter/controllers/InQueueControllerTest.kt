package com.proximity.labs.qcounter.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.proximity.labs.qcounter.data.dto.request.InQueueRequest
import com.proximity.labs.qcounter.data.models.user.User
import com.proximity.labs.qcounter.service.InQueueService
import com.proximity.labs.qcounter.service.QueueService
import com.proximity.labs.qcounter.utils.FakeDataDummy
import com.proximity.labs.qcounter.utils.whenever
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

@SpringBootTest
internal class InQueueControllerTest {

    @Autowired
    private lateinit var inQueueController: InQueueController

    @MockBean
    private lateinit var inQueueService: InQueueService

    @MockBean
    private lateinit var queueService: QueueService

    private lateinit var mockMvc: MockMvc

    private fun putAuthenticationPrincipal(): HandlerMethodArgumentResolver? {
        return object : HandlerMethodArgumentResolver {
            override fun supportsParameter(parameter: MethodParameter): Boolean {
                return parameter.parameterType.isAssignableFrom(User::class.java)
            }

            override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer,
                                         webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory): Any {
                return FakeDataDummy.user()[0]
            }
        }
    }

    @Test
    fun inQueues() {
        whenever(queueService.findFirstByClientGeneratedId(any(String::class.java))).thenReturn(Optional.of(FakeDataDummy.savedQueue()))
        whenever(inQueueService.findByQueueId(any(Long::class.java))).thenReturn(FakeDataDummy.inQueues())
        mockMvc = MockMvcBuilders.standaloneSetup(inQueueController).setCustomArgumentResolvers(putAuthenticationPrincipal()).build()
        val inQRequest = InQueueRequest("clientGeneratedId")
        println(ObjectMapper().writeValueAsString(inQRequest))
        mockMvc
                .perform(MockMvcRequestBuilders.get("/in_queue")
                        .content(ObjectMapper().writeValueAsString(inQRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray)
    }
}