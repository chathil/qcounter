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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.models.user.UserDevice;
import com.proximity.labs.qcounter.data.repositories.UserDeviceRepository;
import com.proximity.labs.qcounter.exception.TokenRefreshException;

@Service
public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    @Autowired
    public UserDeviceService(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    /**
     * Find the user device info by user id
     */
    public Optional<UserDevice> findById(Long userId) {
        return userDeviceRepository.findById(userId);
    }

    /**
     * Find the user device info by refresh token
     */
    public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
        return userDeviceRepository.findByRefreshToken(refreshToken);
    }

    /**
     * Creates a new user device and set the user to the current device
     */
    public UserDevice createUserDevice(String deviceToken, User user) {
        UserDevice userDevice = new UserDevice(deviceToken, user);
        userDevice.setIsRefreshActive(true);
        return userDevice;
    }

    public Optional<UserDevice> findFirstByDeviceToken(String deviceToken) {
        return userDeviceRepository.findFirstByDeviceToken(deviceToken);
    }

    /**
     * Check whether the user device corresponding to the token has refresh enabled and
     * throw appropriate errors to the client
     */
    void verifyRefreshAvailability(RefreshToken refreshToken) {
        UserDevice userDevice = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(), "No device found for the matching token. Please login again"));

        if (!userDevice.getIsRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh blocked for the device. Please login through a different device");
        }
    }
}
