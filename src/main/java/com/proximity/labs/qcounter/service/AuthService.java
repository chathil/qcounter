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
package com.proximity.labs.qcounter.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.proximity.labs.qcounter.data.dto.request.TokenRefreshRequest;
import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.models.user.UserDevice;
import com.proximity.labs.qcounter.exception.ResourceAlreadyInUseException;
import com.proximity.labs.qcounter.exception.TokenRefreshException;
import com.proximity.labs.qcounter.security.JwtTokenProvider;

@Service
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class);
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDeviceService userDeviceService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserService userService, JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
            UserDeviceService userDeviceService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.userDeviceService = userDeviceService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Generates a JWT token for the validated client by userId
     */
    private String generateTokenFromUserId(Long userId) {
        return tokenProvider.generateTokenFromUserId(userId);
    }

    public Optional<User> signUp(String deviceToken, String ipAddress, String name, String email,
            String password) {
        String newRegistrationRequestEmail = email;
        if (emailAlreadyExists(newRegistrationRequestEmail)) {
            logger.error("Email already exists: " + newRegistrationRequestEmail);
            throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
        }

        logger.info("Trying to register new user [" + newRegistrationRequestEmail + "]");
        User newUser = userService.save(new User(name, email, passwordEncoder.encode(password), ipAddress));
        return Optional.ofNullable(newUser);
    }

    public Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    public Optional<Authentication> authenticateUser(String deviceToken, String ipAddress, String email,
            String password) {
        return Optional.ofNullable(
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
    }

    /**
     * Generates a JWT token for the validated client
     */
    public String generateToken(User customUserDetails) {
        return tokenProvider.generateToken(customUserDetails);
    }

    /**
     * Creates and persists the refresh token for the user device. If device exists
     * already, we don't care. Unused devices with expired tokens should be cleaned
     * with a cron job. The generated token would be encapsulated within the jwt.
     * Remove the existing refresh token as the old one should not remain valid.
     */
    public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication,
            String deviceToken, String ipAddress, String email, String password) {

        User currentUser = (User) authentication.getPrincipal();

        userDeviceService.findByUserId(currentUser.getId()).map(UserDevice::getRefreshToken)
                .map(RefreshToken::getId).ifPresent(refreshTokenService::deleteById);
        logger.info(currentUser.getName());

        UserDevice userDevice = userDeviceService.createUserDevice(deviceToken, currentUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userDevice.setRefreshToken(refreshToken);
        refreshToken.setUserDevice(userDevice);
        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    /**
     * Refresh the expired jwt token using a refresh token and device info. The *
     * refresh token is mapped to a specific device and if it is unexpired, can help
     * * generate a new jwt. If the refresh token is inactive for a device or it is
     * expired, * throw appropriate errors.
     */
    public Optional<String> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return Optional.of(refreshTokenService.findByToken(requestRefreshToken).map(refreshToken -> {
            refreshTokenService.verifyExpiration(refreshToken);
            userDeviceService.verifyRefreshAvailability(refreshToken);
            refreshTokenService.increaseCount(refreshToken);
            return refreshToken;
        }).map(RefreshToken::getUserDevice).map(UserDevice::getUser).map(User::getId)
                .map(this::generateTokenFromUserId))
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Missing refresh token in database.Please login again"));
    }
}
