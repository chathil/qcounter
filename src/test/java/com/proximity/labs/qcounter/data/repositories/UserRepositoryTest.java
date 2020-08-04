package com.proximity.labs.qcounter.data.repositories;


import com.proximity.labs.qcounter.data.models.user.User;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class UserRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void whenFindById_thenReturnUser() {

        User user = new User("Test", "test@gmail.com", "password", "192.168.1.1");
        testEntityManager.persist(user);
        testEntityManager.flush();

        Optional<User> found = userRepository.findById(user.getId());

        assertThat(found.isPresent()).isEqualTo(true);
        assertThat(user.getName()).isEqualTo(found.get().getName());
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        boolean existByEmail = userRepository.existsByEmail("chathil98@gmail.com");
        assertThat(existByEmail).isTrue();
    }

    @Test
    public void whenExistsByEmail_thenReturnFalse() {
        boolean existByEmail = userRepository.existsByEmail("chathil00@gmail.com");
        assertThat(existByEmail).isFalse();
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        Optional<User> user = userRepository.findByEmail("chathil98@gmail.com");
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getName()).isEqualTo("Abdul Chathil");
    }
}
