package com.proximity.labs.qcounter.service;


import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.repositories.InQueueRepository;
import com.proximity.labs.qcounter.utils.FakeDataDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class InQueueServiceTest {
    @MockBean
    private InQueueRepository inQueueRepository;
    @MockBean
    private QueueService queueService;
    @MockBean
    private QueueStatsService queueStatsService;

    @Autowired
    private InQueueService inQueueService;

    private Queue fakeQ;

    @BeforeEach
    public void setUp() {
        fakeQ = FakeDataDummy.queue(FakeDataDummy.user().get(0));
        QueueStats fakeStats = new QueueStats(fakeQ);
        fakeStats.setId(1L);
        fakeStats.setCurrentInQueue(15);
        fakeStats.setCurrentQueue(2);
        fakeQ.setId(1L);
        fakeQ.setQueueStats(fakeStats);
        given(queueStatsService.save(any(QueueStats.class))).willAnswer(invocation -> invocation.getArgument(0));
        when(inQueueRepository.findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(any(Long.class), any(Integer.class), any(Integer.class))).thenReturn(FakeDataDummy.inQueues());
    }

    @Test
    public void whenJoinQueueAndPersist_thenReturnPairQueueAndInQueue() {
        when(queueService.findFirstByClientGeneratedId(any(String.class))).thenReturn(java.util.Optional.of(fakeQ));
        given(inQueueRepository.save(any(InQueue.class))).willAnswer(invocation -> invocation.getArgument(0));
        Optional<Pair<Queue, InQueue>> queueInQueuePair = inQueueService.joinQueueAndPersist(FakeDataDummy.user().get(0), FakeDataDummy.joinQueueRequests().get(0));
        assertTrue(queueInQueuePair.isPresent());
        assertEquals(fakeQ.getName(), queueInQueuePair.get().getFirst().getName());
    }

    @Test
    public void whenIncrement_thenIncrement() {
        inQueueService.increment(fakeQ);
        assertEquals(4, fakeQ.getQueueStats().getCurrentQueue());
    }

    @Test
    public void whenIncrement_thenLimitReached() {
        fakeQ.getQueueStats().setCurrentInQueue(100);
        fakeQ.getQueueStats().setCurrentQueue(99);
        inQueueService.increment(fakeQ);
        assertEquals(100, fakeQ.getQueueStats().getCurrentQueue());
    }

    @Test
    public void whenDecrement_thenDecrement() {
        inQueueService.decrement(fakeQ);
        assertEquals(0, fakeQ.getQueueStats().getCurrentQueue());
    }

    @Test
    public void whenDecrement_thenLimitReached() {
        fakeQ.getQueueStats().setCurrentQueue(1);
        inQueueService.decrement(fakeQ);
        assertEquals(0, fakeQ.getQueueStats().getCurrentQueue());
    }

    @Test
    public void whenGetNext_thenReturnListOfPairOfUserIdAndUsername() {
        List<Pair<Long, String>> nextInLine = inQueueService.getNext(fakeQ);
        assertEquals(FakeDataDummy.inQueues().size(), nextInLine.size());
    }

    @Test
    public void whenGetPrev_thenReturnListOfPairOfUserIdAndUsername() {
        List<Pair<Long, String>> nextInLine = inQueueService.getPrev(fakeQ);
        assertEquals(FakeDataDummy.inQueues().size(), nextInLine.size());
    }

    @Test
    public void whenGetCurrent_thenReturnListOfPairOfUserIdAndUsername() {
        List<Pair<Long, String>> nextInLine = inQueueService.getCurrent(fakeQ);
        assertEquals(FakeDataDummy.inQueues().size(), nextInLine.size());
    }
}
