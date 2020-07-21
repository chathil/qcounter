package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.repositories.QueueStatsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueStatsService {
    private final QueueStatsRepository queueStatsRepository;

    @Autowired
    public QueueStatsService(QueueStatsRepository queueStatsRepository) {
        this.queueStatsRepository = queueStatsRepository;
    }

    public QueueStats createQueueStats() {
        QueueStats qStats  = new QueueStats();
        return qStats;
    }

    public QueueStats save(QueueStats qStats) {
        return queueStatsRepository.save(qStats);
    }
    
}