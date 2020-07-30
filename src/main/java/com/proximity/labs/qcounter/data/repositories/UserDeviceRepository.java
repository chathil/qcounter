package com.proximity.labs.qcounter.data.repositories;

import java.util.Optional;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.UserDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findFirstByDeviceToken(String deviceToken);

    Optional<UserDevice> findByUserId(Long userId);

}