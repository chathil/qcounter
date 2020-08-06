package com.proximity.labs.qcounter.controllers;

import com.proximity.labs.qcounter.data.dto.request.AuthRequest;
import com.proximity.labs.qcounter.data.dto.request.SigninRequest;
import com.proximity.labs.qcounter.data.dto.request.SignupRequest;
import com.proximity.labs.qcounter.data.dto.request.TokenRefreshRequest;
import com.proximity.labs.qcounter.data.dto.response.AuthResponse;
import com.proximity.labs.qcounter.data.dto.response.JwtAuthenticationResponse;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.exception.TokenRefreshException;
import com.proximity.labs.qcounter.exception.UserLoginException;
import com.proximity.labs.qcounter.exception.UserRegistrationException;
import com.proximity.labs.qcounter.security.JwtTokenProvider;
import com.proximity.labs.qcounter.service.AuthService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/auth")
@Api(tags = "Authentication", value = "Authorization Rest API, Defines endpoints that can be hit only when the user is not logged in. It's not secured by default.")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider tokenProvider,
                          ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Register new user. On Successfull @signInHelper will be called to authenticate new user right away.
     * doc by @chathil
     *
     * @param signupRequest information to authorized a user
     * @return ResponseEntity
     */
    @ApiOperation(value = "Register new user, then log them in. If succeed return the auth tokens and some other data")
    @PostMapping("/signup")
    public @ResponseBody
    ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest) {

        return authService.signUp(signupRequest).map(user -> signinHelper(signupRequest)).orElseThrow(
                () -> new UserRegistrationException(signupRequest.getEmail(), "Missing user object in database"));
    }

    @ApiOperation(value = "Logs the user in to the system and return the auth tokens and some other data")
    @GetMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody SigninRequest signinRequest) {
        return signinHelper(signinRequest);
    }

    /**
     * Refresh the expired jwt token using a refresh token for the specific device
     * and return a new token to the caller
     * doc by @chathil
     *
     * @param tokenRefreshRequest contains refresh token
     * @return @ResponseEntity
     */
    @GetMapping("/refresh")
    @ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the"
            + "updated response tokens")
    public ResponseEntity<JwtAuthenticationResponse> refreshJwtToken(
            @ApiParam(value = "The TokenRefreshRequest payload") @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        return authService.refreshJwtToken(tokenRefreshRequest).map(updatedToken -> {
            String refreshToken = tokenRefreshRequest.getRefreshToken();
            logger.info("Created new Jwt Auth token: " + updatedToken);
            return ResponseEntity
                    .ok(new JwtAuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
        }).orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(),
                "Unexpected error during token refresh. Please logout and login again."));
    }

    /**
     * Shared method in signin & signup route. This method helps authenticate new user.
     * On successful signin/ signup. this method returns @AuthResponse wrapped in Response Entity.
     * doc by @chathil
     *
     * @param authRequest information to authorized a user
     * @return @ResponseEntity
     */
    public ResponseEntity<AuthResponse> signinHelper(AuthRequest authRequest) {
        Authentication authentication = authService
                .authenticateUser(authRequest.getEmail(),
                        authRequest.getPassword())
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + authRequest.getEmail() + "]"));

        User customUserDetails = (User) authentication.getPrincipal();
        logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService
                .createAndPersistRefreshTokenForDevice(authentication, authRequest.getDeviceToken())
                .map(RefreshToken::getToken).map(refreshToken -> {
                    String accessToken = authService.generateToken(customUserDetails);
                    return ResponseEntity.ok(new AuthResponse(customUserDetails.getId(), customUserDetails.hexId(),
                            authRequest.getDeviceToken(), refreshToken, accessToken, tokenProvider.getExpiryDuration(),
                            customUserDetails.getIpAddress(), customUserDetails.getRoles(), customUserDetails.getName(),
                            customUserDetails.getEmail(), null, customUserDetails.getLocation(),
                            customUserDetails.getProfileCompletion()));
                }).orElseThrow(() -> new UserLoginException(
                        "Couldn't create refresh token for: [" + authRequest.getEmail() + "]"));
    }
}