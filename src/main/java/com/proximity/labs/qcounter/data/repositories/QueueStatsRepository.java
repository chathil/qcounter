package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.QueueStats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueStatsRepository extends JpaRepository<QueueStats, Long> {
    
}