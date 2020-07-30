package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

public class MyQueuesCounterResponse {

    private final String id;
    private final String name;
    private final String desc;
    @JsonProperty("current_num")
    private final int currentNum;
    private final int of;
    @JsonProperty("increment_by")
    private final int incrementBy;
    @JsonProperty("valid_until")
    private final long validUntil;
    @JsonProperty("is_closed_queue")
    private final boolean isClosedQueue;
    @JsonProperty("queue_state")
    private final QueueState queueState;

    public MyQueuesCounterResponse(String id, String name, String desc, int currentNum, int of, int incrementBy, long validUntil, boolean isClosedQueue, QueueState queueState) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.currentNum = currentNum;
        this.of = of;
        this.incrementBy = incrementBy;
        this.validUntil = validUntil;
        this.isClosedQueue = isClosedQueue;
        this.queueState = queueState;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public int getOf() {
        return of;
    }

    public int getIncrementBy() {
        return incrementBy;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public boolean isClosedQueue() {
        return isClosedQueue;
    }

    public QueueState getQueueState() {
        return queueState;
    }
}
