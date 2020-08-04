package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.models.role.Role;
import com.proximity.labs.qcounter.data.models.role.RoleName;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.repositories.UserRepository;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
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
        User savedUser = service.save(user, false);
        assertThat(savedUser.getRoles().size()).isOne();
        assertThat(new ArrayList<>(savedUser.getRoles()).get(0).getRole()).isEqualTo(RoleName.ROLE_USER);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        verify(repository).save(any(User.class));
    }

    @Test
    public void saveAdminUser() {
        final User user = new User("Test", "test@gmail.com", "password", "192.168.1.1");
        given(repository.save(user)).willAnswer(invocation -> invocation.getArgument(0));
        User savedUser = service.save(user, true);
        assertThat(savedUser.getRoles().size()).isEqualTo(2);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        verify(repository).save(any(User.class));
    }

    @Test
    public void loadUserByUsername() {
        when(repository.findByEmail("chathil98@gmail.com")).thenReturn(Optional.of(FakeDataDummy.user().get(0)));
        UserDetails userDetails  = service.loadUserByUsername("chathil98@gmail.com");
        assertThat(userDetails.getUsername()).isEqualTo("chathil98@gmail.com");
    }

    @Test
    public void loadByUserId() {
        when(repository.findById(1L)).thenReturn(Optional.of(FakeDataDummy.user().get(0)));
        UserDetails userDetails  = service.loadUserById(1L);
        assertThat(userDetails.getUsername()).isEqualTo("chathil98@gmail.com");
    }
}
