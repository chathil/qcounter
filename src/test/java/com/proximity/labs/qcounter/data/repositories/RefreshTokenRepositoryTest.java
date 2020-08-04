package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    public void whenFindById_thenReturnRefreshToken() {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(1L);
        assertThat(refreshToken.isPresent()).isTrue();
        assertThat(refreshToken.get().getToken()).isEqualTo("e765a49f-cdab-4db0-bf7e-e8faea9f74421");
    }

    @Test
    public void whenFindByToken_thenReturnRefreshToken() {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken("e765a49f-cdab-4db0-bf7e-e8faea9f74421");
        assertThat(refreshToken.isPresent()).isTrue();
        assertThat(refreshToken.get().getId()).isEqualTo(1L);
    }

}
