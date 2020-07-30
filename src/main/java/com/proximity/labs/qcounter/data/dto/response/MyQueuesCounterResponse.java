package com.proximity.labs.qcounter.data.dto.response;

import com.proximity.labs.qcounter.data.models.queue.QueueState;

public class MyQueuesCounterResponse {

    private final String id;
    private final String name;
    private final String desc;
    private final int currentNum;
    private final int of;
    private final int incrementBy;
    private final long validUntil;
    private final boolean isClosedQueue;
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
