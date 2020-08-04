package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.UserDevice;
import com.proximity.labs.qcounter.data.repositories.UserDeviceRepository;
import com.proximity.labs.qcounter.exception.TokenRefreshException;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserDeviceServiceTest {

    @Autowired
    UserDeviceService userDeviceService;

    @MockBean
    UserDeviceRepository userDeviceRepository;

    @Test
    void whenVerifyRefreshAvailability_thenThrowBlockedTokenRefreshException() {
        when(userDeviceRepository.findByRefreshToken(any(RefreshToken.class))).thenReturn(java.util.Optional.ofNullable(FakeDataDummy.badUserDeviceSet().get(0)));
        TokenRefreshException thrown = assertThrows(
                TokenRefreshException.class,
                () -> userDeviceService.verifyRefreshAvailability(FakeDataDummy.refreshToken(FakeDataDummy.badUserDeviceSet().get(0))),
                "Nothing's thrown, that's weird."
        );
        assertTrue(thrown.getMessage().contains("Refresh blocked for the device. Please login through a different device"));
    }

    @Test
    void whenVerifyRefreshAvailability_thenThrowNoDeviceTokenRefreshException() {
        when(userDeviceRepository.findByRefreshToken(any(RefreshToken.class))).thenReturn(Optional.empty());
        TokenRefreshException thrown = assertThrows(
                TokenRefreshException.class,
                () -> userDeviceService.verifyRefreshAvailability(FakeDataDummy.refreshToken(FakeDataDummy.badUserDeviceSet().get(0))),
                "Nothing's thrown, that's weird."
        );
        assertTrue(thrown.getMessage().contains("No device found for the matching token. Please login again"));
    }
}