package com.proximity.labs.qcounter.service;

import java.util.Optional;

import com.proximity.labs.qcounter.component.CounterHandler;
import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
// import com.proximity.labs.qcounter.data.repositories.InQueueRepository;
import com.proximity.labs.qcounter.data.repositories.InQueueRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class InQueueService {
    private final InQueueRepository inQueueRepository;
    private final QueueService qService;
    private final QueueStatsService qStatsService;
    private static final Logger logger = Logger.getLogger(InQueueService.class);

    @Autowired
    public InQueueService(InQueueRepository inQueueRepository, QueueService qService, QueueStatsService qStatsService) {
        this.inQueueRepository = inQueueRepository;
        this.qService = qService;
        this.qStatsService = qStatsService;
    }

    public Optional<Pair<Queue, InQueue>> joinQueueAndPersist(User currentUser, JoinQueueRequest joinQueueRequest) {
        Optional<Queue> findQ = qService.findFirstByClientGeneratedId(joinQueueRequest.getQueueId());

        if (findQ.isEmpty()) {
            return Optional.empty();
        }

        Queue qToJoin = findQ.get();
        QueueStats qStats = qToJoin.getQueueStats();
        qStats.setCurrentInQueue(qStats.getCurrentInQueue() + 1);
        qStats = qStatsService.save(qStats);
        InQueue inQueue = new InQueue(qToJoin, currentUser, joinQueueRequest.getFullName(),
                joinQueueRequest.getContact(), qStats.getCurrentInQueue());
        inQueue = inQueueRepository.save(inQueue);
        
        return Optional.of(Pair.of(qToJoin, inQueue));
    }

    public void increment() {
        logger.info("incrementing");
    }

    public void decrement() {
        logger.info("decrementing");
    }

}