package com.proximity.labs.qcounter.controllers;

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
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/auth") // annotation processor
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

	@PostMapping("/signup")
	public @ResponseBody ResponseEntity signup(@RequestParam("device_token") String deviceToken,
			@RequestParam("ip_address") String ipAddress, @RequestParam String name, @RequestParam String email,
			@RequestParam String password) {

		return authService.signUp(deviceToken, ipAddress, name, email, password).map(user -> {

			Authentication authentication = authService.authenticateUser(deviceToken, ipAddress, email, password)
					.orElseThrow(() -> new UserLoginException("Couldn't login user [" + email + "]"));

			User customUserDetails = (User) authentication.getPrincipal();

			logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			logger.info("before jwt token");
			String accessToken = authService.generateToken(customUserDetails);
			logger.info(accessToken);
			return authService
					.createAndPersistRefreshTokenForDevice(authentication, deviceToken, ipAddress, email, password)
					.map(RefreshToken::getToken).map(refreshToken -> {
						return ResponseEntity.ok(new AuthResponse(customUserDetails.getId(),
								customUserDetails.hexId(), deviceToken, refreshToken, accessToken, tokenProvider.getExpiryDuration(),
								customUserDetails.getIpAddress(), customUserDetails.getName(),
								customUserDetails.getEmail(), null, customUserDetails.getLocation(),
								customUserDetails.getAccountType(), customUserDetails.getProfileCompletion()));
					}).orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + email + "]"));
		}).orElseThrow(() -> new UserRegistrationException(email, "Missing user object in database"));
	}

	@GetMapping("/signin")
	public ResponseEntity signin(@RequestParam("device_token") String deviceToken,
			@NonNull @RequestParam("ip_address") String ipAddress, @RequestParam String email,
			@RequestParam String password) {

		Authentication authentication = authService.authenticateUser(deviceToken, ipAddress, email, password)
				.orElseThrow(() -> new UserLoginException("Couldn't login user [" + email + "]"));

		User customUserDetails = (User) authentication.getPrincipal();
		logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return authService
				.createAndPersistRefreshTokenForDevice(authentication, deviceToken, ipAddress, email, password)
				.map(RefreshToken::getToken).map(refreshToken -> {
					String accessToken = authService.generateToken(customUserDetails);
					return ResponseEntity.ok(new AuthResponse(customUserDetails.getId(), customUserDetails.hexId(),
							deviceToken, refreshToken, accessToken, tokenProvider.getExpiryDuration(), customUserDetails.getIpAddress(),
							customUserDetails.getName(), customUserDetails.getEmail(), null, // get profile pic with
																								// hash
							customUserDetails.getLocation(), customUserDetails.getAccountType(),
							customUserDetails.getProfileCompletion()));
				}).orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + email + "]"));
	}

	/**
     * Refresh the expired jwt token using a refresh token for the specific device
     * and return a new token to the caller
     */
    @GetMapping("/refresh")
    @ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the" +
            "updated response tokens")
    public ResponseEntity refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest payload") @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updatedToken -> {
                    String refreshToken = tokenRefreshRequest.getRefreshToken();
                    logger.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "Unexpected error during token refresh. Please logout and login again."));
    }

}