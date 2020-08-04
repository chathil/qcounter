package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.repositories.RefreshTokenRepository;
import com.proximity.labs.qcounter.exception.TokenRefreshException;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenServiceTest {

    @Autowired
    RefreshTokenService refreshTokenService;

    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    @Test
    void verifyExpiration() {
        TokenRefreshException thrown = assertThrows(
                TokenRefreshException.class,
                () -> refreshTokenService.verifyExpiration(FakeDataDummy.badRefreshToken(FakeDataDummy.userDeviceSet().get(0))),
                "Nothing's thrown, that's weird."
        );
        assertTrue(thrown.getMessage().contains("Expired token. Please issue a new request"));
    }
}