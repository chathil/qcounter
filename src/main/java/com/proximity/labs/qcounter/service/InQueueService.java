package com.proximity.labs.qcounter.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest;
import com.proximity.labs.qcounter.data.dto.request.RemoveFromInQueueRequest;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
// import com.proximity.labs.qcounter.data.repositories.InQueueRepository;
import com.proximity.labs.qcounter.data.repositories.InQueueRepository;

import com.proximity.labs.qcounter.exception.AppException;
import com.proximity.labs.qcounter.exception.ResourceNotFoundException;
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
        Queue qToJoin = qService.findFirstByClientGeneratedId(joinQueueRequest.getQueueId()).orElseThrow(() -> new AppException("No such queue"));
        return Optional.of(Pair.of(qToJoin, getInLine(qToJoin, currentUser, joinQueueRequest)));
    }

    public Optional<Pair<Queue, InQueue>> addToQueueAndPersist(User currentUser, JoinQueueRequest joinQueueRequest) {
        Queue qToJoin = qService.findFirstByClientGeneratedId(joinQueueRequest.getQueueId()).orElseThrow(() -> new AppException("No such queue"));
        if (!qToJoin.getOwner().getId().equals(currentUser.getId()))
            throw new AppException(String.format("Queue with id %s doesn't belongs to %s", joinQueueRequest.getQueueId(), currentUser.getName()));
        return Optional.of(Pair.of(qToJoin, getInLine(qToJoin, currentUser, joinQueueRequest)));
    }

    private InQueue getInLine(Queue qToJoin, User currentUser, JoinQueueRequest joinQueueRequest) {
        QueueStats qStats = qToJoin.getQueueStats();
        qStats.setCurrentInQueue(qStats.getCurrentInQueue() + 1);
        qStats = qStatsService.save(qStats);
        InQueue inQueue = new InQueue(qToJoin, currentUser, joinQueueRequest.getFullName(),
                joinQueueRequest.getContact(), qStats.getCurrentInQueue());
        return inQueueRepository.save(inQueue);
    }

    public void increment(Queue queue) {
        QueueStats qStats = queue.getQueueStats();
        if (qStats.getCurrentQueue() < queue.getMaxCapacity()) {
            int currentQueue = qStats.getCurrentQueue() + queue.getIncrementBy();
            qStats.setCurrentQueue(currentQueue > queue.getQueueStats().getCurrentInQueue() ? queue.getQueueStats().getCurrentInQueue() : currentQueue);
        }
        qStatsService.save(qStats);
    }

    public void decrement(Queue queue) {
        QueueStats qStats = queue.getQueueStats();
        if (qStats.getCurrentQueue() > 0) {
            int upperBound = qStats.getCurrentQueue() - queue.getIncrementBy();
            qStats.setCurrentQueue(Math.max(upperBound, 0));
            qStatsService.save(qStats);
        }
    }

    public List<Pair<Long, String>> getNext(Queue queue) {
        QueueStats qStats = queue.getQueueStats();
        int currentQueue = qStats.getCurrentQueue() + queue.getIncrementBy();
        Set<InQueue> inQs = inQueueRepository.findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(queue.getId(), qStats.getCurrentQueue(), currentQueue);
        return inQs.stream().map(inQ -> Pair.of(inQ.getUser().getId(), inQ.getUser().getName())).collect(Collectors.toList());
    }

    public List<Pair<Long, String>> getPrev(Queue queue) {
        QueueStats qStats = queue.getQueueStats();
        int upperBound = qStats.getCurrentQueue() - queue.getIncrementBy();
        int lowerBound = qStats.getCurrentQueue() - queue.getIncrementBy() * 2;
        Set<InQueue> inQs = inQueueRepository.findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(queue.getId(), lowerBound, upperBound);
        return inQs.stream().map(inQ -> Pair.of(inQ.getUser().getId(), inQ.getUser().getName())).collect(Collectors.toList());
    }

    public List<Pair<Long, String>> getCurrent(Queue queue) {
        QueueStats qStats = queue.getQueueStats();
        int lowerBound = qStats.getCurrentQueue() - queue.getIncrementBy();
        Set<InQueue> inQs = inQueueRepository.findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(queue.getId(), lowerBound, qStats.getCurrentQueue());
        return inQs.stream().map(inQ -> Pair.of(inQ.getUser().getId(), inQ.getUser().getName())).collect(Collectors.toList());
    }

    public Optional<InQueue> findUserInQueue(long queueId, long userId) {
        return inQueueRepository.findFirstByQueueIdAndUserId(queueId, userId);
    }

    public void removeFromInQueue(User user, RemoveFromInQueueRequest removeFromInQueueRequest) {
        qService.findFirstByClientGeneratedId(removeFromInQueueRequest.getQueueId()).map(queue -> {
            if (!queue.getOwner().getId().equals(user.getId()))
                throw new AppException(String.format("Queue with id %s doesn't belongs to %s", removeFromInQueueRequest.getQueueId(), user.getName()));
            inQueueRepository.deleteById(removeFromInQueueRequest.getId());
            return true;
        }).orElseThrow(() -> new ResourceNotFoundException("Queue", "queue_id", removeFromInQueueRequest.getQueueId()));
    }

    public Set<InQueue> findUserInQueues(long userId) {
        return inQueueRepository.findByUserId(userId);
    }

    public Set<InQueue> findByQueueId(long queueId) {
        return inQueueRepository.findByQueueId(queueId);
    }


}