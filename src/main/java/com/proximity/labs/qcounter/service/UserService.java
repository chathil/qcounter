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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.proximity.labs.qcounter.data.models.role.Role;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.models.user.UserDevice;
import com.proximity.labs.qcounter.data.repositories.UserRepository;
import com.proximity.labs.qcounter.exception.UserLogoutException;

@Service
public class UserService implements UserDetailsService{

    private static final Logger logger = Logger.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserDeviceService userDeviceService;
    private final RefreshTokenService refreshTokenService;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, UserDeviceService userDeviceService, RoleService roleService ,RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.userDeviceService = userDeviceService;
        this.refreshTokenService = refreshTokenService;
        this.roleService = roleService;
    }

    /**
     * Finds a user in the database by username
     */
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * Finds a user in the database by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find a user in db by id.
     */
    public Optional<User>findById(Long Id) {
        return userRepository.findById(Id);
    }

    /**
     * Save the user to the database
     */
    public User save(User user) {
        Boolean isNewUserAsAdmin = false;
        user.addRoles(getRolesForNewUser(isNewUserAsAdmin));

        return userRepository.save(user);
    }

    /**
     * Check is the user exists given the email: naturalId
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Log the given user out and delete the refresh token associated with it. If no device
     * id is found matching the database for the given user, throw a log out exception.
     */
    public void logout(String deviceToken) {

        UserDevice userDevice = userDeviceService.findFirstByDeviceToken(deviceToken)
                .orElseThrow(() -> new UserLogoutException(deviceToken, "Invalid device token supplied. No matching device found for the given user "));

        logger.info("Removing refresh token associated with device [" + userDevice + "]");
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findByEmail(email);
        logger.info("Fetched user : " + dbUser + " by " + email);
        return dbUser.map(User::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user email in the database for " + email));
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> dbUser = userRepository.findById(id);
        logger.info("Fetched user : " + dbUser + " by " + id);
        return dbUser.map(User::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
    }

    /**
     * Performs a quick check to see what roles the new user could be assigned to.
     *
     * @return list of roles for the new user
     */
    private Set<Role> getRolesForNewUser(Boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin) {
            newUserRoles.removeIf(Role::isAdminRole);
        }
        logger.info("Setting user roles: " + newUserRoles);
        return newUserRoles;
    }
}
