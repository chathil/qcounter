package com.proximity.labs.qcounter.cache;

import com.proximity.labs.qcounter.event.OnUserLogoutSuccessEvent;
import com.proximity.labs.qcounter.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoggedOutJwtTokenCacheTest {

    @Mock
    private JwtTokenProvider mockTokenProvider;

    private LoggedOutJwtTokenCache cache;


    @Test
    public void testMarkLogoutEventInsertsOnlyOnce() {
        this.cache = new LoggedOutJwtTokenCache(10, mockTokenProvider);
        OnUserLogoutSuccessEvent event = stubLogoutEvent("U1", "T1");
        when(mockTokenProvider.getTokenExpiryFromJWT("T1")).thenReturn(Date.from(Instant.now().plusSeconds(100)));

        cache.markLogoutEventForToken(event);
        cache.markLogoutEventForToken(event);
        cache.markLogoutEventForToken(event);
        verify(mockTokenProvider, times(1)).getTokenExpiryFromJWT("T1");

    }

    @Test
    public void getLogoutEventForToken() {
        this.cache = new LoggedOutJwtTokenCache(10, mockTokenProvider);
        OnUserLogoutSuccessEvent event = stubLogoutEvent("U2", "T2");
        when(mockTokenProvider.getTokenExpiryFromJWT("T2")).thenReturn(Date.from(Instant.now().plusSeconds(10)));

        cache.markLogoutEventForToken(event);
        assertNull(cache.getLogoutEventForToken("T1"));
        assertNotNull(cache.getLogoutEventForToken("T2"));
    }

    private OnUserLogoutSuccessEvent stubLogoutEvent(String email, String token) {
        this.cache = new LoggedOutJwtTokenCache(10, mockTokenProvider);
        return new OnUserLogoutSuccessEvent(email, token);
    }
}