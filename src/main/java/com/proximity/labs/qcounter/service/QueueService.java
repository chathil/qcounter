package com.proximity.labs.qcounter.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueState;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.repositories.QueueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    private final QueueRepository queueRepository;
    private final QueueStatsService qStatsService;

    @Autowired
    public QueueService(QueueRepository queueRepository, QueueStatsService qStatsService) {
        this.queueRepository = queueRepository;
        this.qStatsService = qStatsService;
    }

    public Optional<Queue> createQueueAndPersist(User owner, NewQueueRequest nQueueRequest) {
        QueueStats qStats = qStatsService.createQueueStats();
        Queue queue = createQueue(owner, nQueueRequest);
        queue.setQueueStats(qStats);
        qStats.setQueue(queue);
        queue = queueRepository.save(queue);
        return Optional.of(queue);
    }

    public Queue createQueue(User owner, NewQueueRequest nQueueRequest) {
        Date validUntil = Date.from(Instant.now().plusMillis(nQueueRequest.getValidFor()));
        return new Queue(owner, nQueueRequest.getClientGeneratedId(), nQueueRequest.getName(), nQueueRequest.getDesc(),
        nQueueRequest.getMax(), nQueueRequest.getIncrementBy(), validUntil, nQueueRequest.getContact(),
        nQueueRequest.isClosedQueue(), "Indonesia ");
    }

    public Optional<Queue> findFirstByClientGeneratedId(String clientGeneratedId) {
        return queueRepository.findFirstByClientGeneratedId(clientGeneratedId);
    }

    public Queue save(Queue queue) {
        return queueRepository.save(queue);
    }

    public Set<Queue> findByUserOwnerId(long userId) {
        return queueRepository.findByOwnerId(userId);
    }
}