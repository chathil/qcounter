package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@SpringBootTest()
@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceTest {
    @MockBean
    private UserRepository repository;
    @Autowired
    private UserService service;

    @Test
    public void saveUser() {
        final User user = new User("Test", "test@gmail.com", "password", "192.168.1.1");
        given(repository.save(user)).willAnswer(invocation -> invocation.getArgument(0));
        User savedUser = service.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        verify(repository).save(any(User.class));
    }
}
