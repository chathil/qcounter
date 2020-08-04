package com.proximity.labs.qcounter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CounterSocketServiceTest {

    @Autowired
    CounterSocketService counterSocketService;

    @Test
    void initialBroadcastMessage() {
    }

    @Test
    void notifyHomeSession() {
    }

    @Test
    void notifyControlSession() {
    }

    @Test
    void verifyOwnership() {
    }
}