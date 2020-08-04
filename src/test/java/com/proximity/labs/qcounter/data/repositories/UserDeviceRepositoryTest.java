package com.proximity.labs.qcounter.data.repositories;


import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.UserDevice;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class UserDeviceRepositoryTest {
    @Autowired
    UserDeviceRepository userDeviceRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    public void whenFindByRefreshToken_thenReturnDevice() {
        RefreshToken refreshToken = refreshTokenRepository.findById(1L).get();
        Optional<UserDevice> deviceOptional = userDeviceRepository.findByRefreshToken(refreshToken);
        assertThat(deviceOptional.isPresent()).isTrue();
        assertThat(deviceOptional.get().getRefreshToken().getToken()).isEqualTo(refreshToken.getToken());
    }

    @Test
    public void whenFindFirstByDeviceToken_thenReturnDevice() {
        Optional<UserDevice> deviceOptional = userDeviceRepository.findFirstByDeviceToken("eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ1");
        assertThat(deviceOptional.isPresent()).isTrue();
        assertThat(deviceOptional.get().getDeviceToken()).isEqualTo("eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ1");
    }
}
