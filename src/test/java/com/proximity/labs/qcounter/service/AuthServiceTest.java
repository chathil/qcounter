package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.dto.request.SignupRequest;
import com.proximity.labs.qcounter.data.dto.request.TokenRefreshRequest;
import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.exception.ResourceAlreadyInUseException;
import com.proximity.labs.qcounter.security.JwtTokenProvider;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Autowired
    AuthService authService;
    @MockBean
    UserService userService;

    SignupRequest signupRequest = new SignupRequest();

    @MockBean
    Authentication authentication;
    @MockBean
    UserDeviceService userDeviceService;
    @MockBean
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        signupRequest.setDeviceToken("deviceToken");
        signupRequest.setEmail("chathil98@gmail.com");
        signupRequest.setIpAddress("192.168.1.1");
        signupRequest.setName("Abdul Chathil");
        signupRequest.setPassword("password");
        given(userService.save(new User("Abdul Chathil", "chathil98@gmail.com", "password", "192.168.1.1"), false)).willAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void whenSignUp_thenReturnSuccessfullySavedUser() {
        when(userService.existsByEmail("chathil98@gmail.com")).thenReturn(false);
        Optional<User> user = authService.signUp(signupRequest);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getName()).isEqualTo("Abdul Chathil");
    }

    @Test
    public void whenSignUp_thenThrowEmailAlreadyUsedError() {
        ResourceAlreadyInUseException thrown = assertThrows(
                ResourceAlreadyInUseException.class,
                () -> {
                    when(userService.existsByEmail("chathil98@gmail.com")).thenReturn(true);
                    Optional<User> user = authService.signUp(signupRequest);
                    assertThat(user.isPresent()).isFalse();
                }, "Expected to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains(String.format("%s already in use with %s : '%s'", "Email", "Address", signupRequest.getEmail())));
    }

    @Test
    public void whenCreateAndPersistRefreshTokenForDevice_thenReturnRefreshToken() {
        when(authentication.getPrincipal()).thenReturn(FakeDataDummy.user().get(0));
        given(userDeviceService.createUserDevice(any(String.class), any(User.class))).willReturn(FakeDataDummy.userDeviceSet().get(0));
        given(userDeviceService.findById(any(Long.class))).willReturn(Optional.empty());
        when(refreshTokenService.save(any(RefreshToken.class))).thenReturn(FakeDataDummy.refreshToken(FakeDataDummy.userDeviceSet().get(0)));
        when(refreshTokenService.createRefreshToken()).thenReturn(FakeDataDummy.refreshToken());
        Optional<RefreshToken> refreshToken = authService
                .createAndPersistRefreshTokenForDevice(authentication, "deviceToken");
        assertTrue(refreshToken.isPresent());
        assertThat(refreshToken.get().getId()).isEqualTo(FakeDataDummy.refreshToken(FakeDataDummy.userDeviceSet().get(0)).getId());
        System.out.println("refreshToken = " + refreshToken.toString());
    }

    @Test
    public void whenRefreshJwtToken_thenReturnTokenString() {
        RefreshToken refreshToken = FakeDataDummy.refreshToken(FakeDataDummy.userDeviceSet().get(0));
        when(refreshTokenService.findByToken(any(String.class))).thenReturn(Optional.of(refreshToken));
        Optional<String> token = authService.refreshJwtToken(new TokenRefreshRequest(refreshToken.getToken()));
        assertTrue(token.isPresent());
        assertEquals(token.get(), tokenProvider.generateTokenFromUserId(1L));
    }
}
