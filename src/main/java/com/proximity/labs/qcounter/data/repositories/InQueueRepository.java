package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.queue.InQueue;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InQueueRepository extends JpaRepository<InQueue, Long> {
    Set<InQueue> findByQueueIdAndQueueNumGreaterThanAndQueueNumIsLessThanEqualOrderByQueueNum(long queueId, int fromQueueNum, int toQueueNum);
    Optional<InQueue> findFirstByQueueIdAndUserId(long queueId, long userId);
    Set<InQueue> findByQueueId(long queueId);
    Set<InQueue> findByUserId(long userId);
    @Modifying
    @Query("DELETE FROM InQueue inq WHERE inq.id in ?1")
    @Transactional
    void deleteById(long id);
}