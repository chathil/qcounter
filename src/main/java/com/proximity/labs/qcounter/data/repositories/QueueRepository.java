package com.proximity.labs.qcounter.data.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.proximity.labs.qcounter.data.models.queue.Queue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    Optional<Queue> findFirstByClientGeneratedId(String clientGeneratedId);

    Set<Queue> findByOwnerId(long userId);
}