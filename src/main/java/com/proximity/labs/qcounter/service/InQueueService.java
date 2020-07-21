package com.proximity.labs.qcounter.service;

import com.proximity.labs.qcounter.data.models.queue.InQueue;
// import com.proximity.labs.qcounter.data.repositories.InQueueRepository;
import com.proximity.labs.qcounter.data.repositories.InQueueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InQueueService {
    private final InQueueRepository inQueueRepository;

    @Autowired
    public InQueueService(InQueueRepository inQueueRepository) {
        this.inQueueRepository = inQueueRepository;
    }

    public InQueue save(InQueue inQueue) {
        return inQueueRepository.save(inQueue);
    }
}