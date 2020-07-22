package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

public class GuestCounterResponse {
    private Long id;
    @JsonProperty("current_num")
    private int currentNum;
    @JsonProperty("in_queue")
    private int inQueue;
    @JsonProperty("max_queue")
    private int maxQueue;
    @JsonProperty("queue_state")
    private QueueState queueState;

    public GuestCounterResponse(Long id, int currentNum, int inQueue, int maxQueue, QueueState queueState) {
        this.id = id;
        this.currentNum = currentNum;
        this.inQueue = inQueue;
        this.maxQueue = maxQueue;
        this.queueState = queueState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public int getInQueue() {
        return inQueue;
    }

    public void setInQueue(int inQueue) {
        this.inQueue = inQueue;
    }

    public int getMaxQueue() {
        return maxQueue;
    }

    public void setMaxQueue(int maxQueue) {
        this.maxQueue = maxQueue;
    }

    public QueueState getQueueState() {
        return queueState;
    }

    public void setQueueState(QueueState queueState) {
        this.queueState = queueState;
    }
}