package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.InQueue;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InQueueRepository extends JpaRepository<InQueue, Long> {
    List<InQueue> findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(long queueId, int fromQueueNum, int toQueueNum);
    Optional<InQueue> findByQueueIdAndUserId(long queueId, long userId);
    List<InQueue> findByQueueId(long queueId);
}