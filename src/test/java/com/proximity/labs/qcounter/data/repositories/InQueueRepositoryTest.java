package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class InQueueRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    InQueueRepository inQueueRepository;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void whenFindByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum_thenReturnInQueues() {
        User owner = userRepository.findByEmail("mark@gmail.com").get();
        User client = userRepository.findByEmail("gabrielle@gmail.com").get();
        Queue queue = FakeDataDummy.queue(owner);
        Queue saved = testEntityManager.persist(queue);
        testEntityManager.flush();
        InQueue inQueue = new InQueue(saved, client, "Name", "contact", 1);
        testEntityManager.persist(inQueue);
        testEntityManager.flush();
        Set<InQueue> inQueues = inQueueRepository.findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(saved.getId(), 0, 2);
        assertThat(inQueues.isEmpty()).isFalse();
        assertThat(inQueues.size()).isEqualTo(1);
        assertThat(new ArrayList<>(inQueues).get(0).getName()).isEqualTo(inQueue.getName());
    }
    @Test
    public void whenFindFirstByQueueIdAndUserId_thenReturnInQueue() {
       Optional<InQueue> inQ = inQueueRepository.findFirstByQueueIdAndUserId(3, 1);
       assertThat(inQ.isPresent()).isTrue();
       assertThat(inQ.get().getName()).isEqualTo("Abdul Chathil");
    }

    @Test
    public void whenFindByQueueId_thenReturnInQueues() {
        Set<InQueue> inQs = inQueueRepository.findByQueueId(3L);
        assertThat(inQs.isEmpty()).isFalse();
        assertThat(inQs.size()).isEqualTo(9);
        assertThat(new ArrayList<>(inQs).get(0).getName()).isEqualTo("Abdul Chathil");
    }

    @Test
    public void whenFindByUserId_thenReturnInQueues() {
        Set<InQueue> inQs = inQueueRepository.findByUserId(6L);
        assertThat(inQs.isEmpty()).isFalse();
        assertThat(inQs.size()).isEqualTo(2);
        assertThat(new ArrayList<>(inQs).get(0).getName()).isEqualTo("Lakeesha from the Hood");
    }
}
