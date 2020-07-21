package com.proximity.labs.qcounter.service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
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

    public Optional<Queue> createQueue(NewQueueRequest nQueueRequest) {
        Date validUntil = Date.from(Instant.now().plusMillis(nQueueRequest.getValidFor()));
        QueueStats qStats = qStatsService.createQueueStats();
        Queue queue = new Queue(nQueueRequest.getClientGeneratedId(), nQueueRequest.getName(), nQueueRequest.getDesc(),
                nQueueRequest.getMax(), nQueueRequest.getIncrementBy(), validUntil, nQueueRequest.getContact(),
                nQueueRequest.isClosedQueue(), "Indonesia ");
        queue.setQueueStats(qStats);
        qStats.setQueue(queue);
        queue = queueRepository.save(queue);
        return Optional.ofNullable(queue);
    }
}