package com.proximity.labs.qcounter.data.models.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserDeviceRepository extends CrudRepository<UserDeviceEntity, Integer> {
    @Transactional
    Long deleteFirstByDeviceToken(String deviceToken);
}