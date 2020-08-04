/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.proximity.labs.qcounter.security;

import com.proximity.labs.qcounter.cache.LoggedOutJwtTokenCache;
import com.proximity.labs.qcounter.event.OnUserLogoutSuccessEvent;
import com.proximity.labs.qcounter.exception.InvalidTokenRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class JwtTokenValidatorTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 2500;

    @Mock
    private LoggedOutJwtTokenCache loggedOutTokenCache;

    private JwtTokenProvider tokenProvider;

    private JwtTokenValidator tokenValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.tokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
        this.tokenValidator = new JwtTokenValidator(jwtSecret, loggedOutTokenCache);
    }

    @Test
    public void testValidateTokenThrowsException_whenTokenIsDamaged() {
        InvalidTokenRequestException thrown = assertThrows(
                InvalidTokenRequestException.class,
                () -> {
                    String token = tokenProvider.generateTokenFromUserId(100L);
                    OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U1", token);
                    when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);
                    tokenValidator.validateToken(token + "-Damage");
                },
                "Expected to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Incorrect signature"));
    }

    @Test
    public void testValidateTokenThrowsException_whenTokenIsExpired() throws InterruptedException {
        InvalidTokenRequestException thrown = assertThrows(
                InvalidTokenRequestException.class,
                () -> {
                    String token = tokenProvider.generateTokenFromUserId(123L);
                    TimeUnit.MILLISECONDS.sleep(jwtExpiryInMs);
                    OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U1", token);
                    when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);
                    tokenValidator.validateToken(token);
                },
                "Expected to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Token expired. Refresh required"));
    }

    @Test
    public void testValidateTokenWorks_whenItIsNotPresentInTokenCache() {
        String token = tokenProvider.generateTokenFromUserId(100L);
        tokenValidator.validateToken(token);
        verify(loggedOutTokenCache, times(1)).getLogoutEventForToken(token);
    }

    private OnUserLogoutSuccessEvent stubLogoutEvent(String email, String token) {
        return new OnUserLogoutSuccessEvent(email, token);
    }
}
