package com.proximity.labs.qcounter.controllers;

import com.proximity.labs.qcounter.service.QueueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class QueueControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private QueueService queueService;

    @Test
    public void createQueue_thenReturnJson() throws Exception {

    }

}
