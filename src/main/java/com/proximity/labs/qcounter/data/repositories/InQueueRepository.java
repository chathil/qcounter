package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.InQueue;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InQueueRepository extends JpaRepository<InQueue, Long> {
    List<InQueue> findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(long queueId, int fromQueueNum, int toQueueNum);
    Optional<InQueue> findByQueueIdAndUserId(long queueId, long userId);
    Set<InQueue> findByQueueId(long queueId);
    Set<InQueue> findByUserId(long userId);
}