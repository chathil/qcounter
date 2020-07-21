package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.InQueue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InQueueRepository extends JpaRepository<InQueue, Long> {
    
}