package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.repositories.QueueRepository;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
@ExtendWith(MockitoExtension.class)
@Transactional
public class QueueServiceTest {
    @MockBean
    private QueueRepository repository;

    @MockBean
    private QueueStatsService statsService;

    @Autowired
    private QueueService service;

    @Autowired
    private UserService userService;

    @Test
    public void createQueueTest() {
        User savedUser = userService.findByEmail("chathil98@gmail.com").get();
        Queue queue = service.createQueue(savedUser, FakeDataDummy.queueRequest());
        assertThat(queue).isNotNull();
        assertThat(queue.getName()).isEqualTo(FakeDataDummy.queueRequest().getName());
    }

    @Test
    public void createQueueAndPersistTest() {
        User savedUser = userService.findByEmail("chathil98@gmail.com").get();
        Queue queue = service.createQueue(savedUser, FakeDataDummy.queueRequest());
        given(statsService.createQueueStats()).willCallRealMethod();
        given(repository.save(queue)).willAnswer(invocation -> invocation.getArgument(0));
        QueueStats qStats = statsService.createQueueStats();
        queue.setQueueStats(qStats);
        qStats.setQueue(queue);
        Queue savedQueue = service.save(queue);
        assertThat(savedQueue).isNotNull();
        assertThat(savedQueue.getName()).isEqualTo(FakeDataDummy.queueRequest().getName());
        verify(repository).save(any(Queue.class));
    }
}
