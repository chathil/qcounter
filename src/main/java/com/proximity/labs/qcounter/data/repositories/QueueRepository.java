package com.proximity.labs.qcounter.data.repositories;

import java.util.List;
import java.util.Optional;

import com.proximity.labs.qcounter.data.models.queue.Queue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    Optional<Queue> findFirstByClientGeneratedId(String clientGeneratedId);

    List<Queue> findByOwnerId(long userId);
}