package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.Queue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    
}