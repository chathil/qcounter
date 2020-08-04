package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.Queue;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class QueueRepositoryTest {

    @Autowired
    QueueRepository queueRepository;

    @Test
    public void whenFindFirstByClientGeneratedId_thenReturnQueue() {
        Optional<Queue> queue = queueRepository.findFirstByClientGeneratedId("VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g1");
        assertThat(queue.isPresent()).isTrue();
        assertThat(queue.get().getClientGeneratedId()).isEqualTo("VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g1");
    }

    @Test
    public void whenFindByOwnerId_thenReturnQueues() {
        Set<Queue> queues = queueRepository.findByOwnerId(1L);
        assertThat(queues.size()).isEqualTo(2);
        List<Queue> queueList = new ArrayList<>(queues);
        assertThat(queueList.get(0).getOwner().getName()).isEqualTo("Abdul Chathil");
        assertThat(queueList.get(1).getOwner().getName()).isEqualTo("Abdul Chathil");
    }
}
