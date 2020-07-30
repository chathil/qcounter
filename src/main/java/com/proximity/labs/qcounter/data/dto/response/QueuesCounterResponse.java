package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

public class QueuesCounterResponse {

    private final String id;
    private final String name;
    @JsonProperty("created_by")
    private final String createdBy;
    @JsonProperty("creator_id")
    private final long creatorId;
    private final String desc;
    private final int max;
    @JsonProperty("current_num")
    private final int currentNum;
    @JsonProperty("my_num")
    private final int myNum;
    @JsonProperty("increment_by")
    private final int incrementBy;
    @JsonProperty("valid_until")
    private final long validUntil;
    private final String contact;
    @JsonProperty("is_closed_queue")
    private final boolean isClosedQueue;
    private final QueueState state;

    public QueuesCounterResponse(String id, String name, String createdBy, long creatorId, String desc, int max, int currentNum, int myNum, int incrementBy, long validUntil, String contact, boolean isClosedQueue, QueueState state) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.creatorId = creatorId;
        this.desc = desc;
        this.max = max;
        this.currentNum = currentNum;
        this.myNum = myNum;
        this.incrementBy = incrementBy;
        this.validUntil = validUntil;
        this.contact = contact;
        this.isClosedQueue = isClosedQueue;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public String getDesc() {
        return desc;
    }

    public int getMax() {
        return max;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public int getMyNum() {
        return myNum;
    }

    public int getIncrementBy() {
        return incrementBy;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public String getContact() {
        return contact;
    }

    public boolean isClosedQueue() {
        return isClosedQueue;
    }

    public QueueState getState() {
        return state;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}