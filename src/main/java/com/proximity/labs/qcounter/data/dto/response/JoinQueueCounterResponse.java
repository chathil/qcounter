package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketMessage;

public class JoinQueueCounterResponse {

    private String id;

    @JsonProperty("current_queue")
    private int currentQueue;
    @JsonProperty("current_in_queue")
    private int currentInQueue;

    public JoinQueueCounterResponse(String id, int currentQueue, int currentInQueue) {
        this.id = id;
        this.currentQueue = currentQueue;
        this.currentInQueue = currentInQueue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrentQueue() {
        return currentQueue;
    }

    public void setCurrentQueue(int currentQueue) {
        this.currentQueue = currentQueue;
    }

    public int getCurrentInQueue() {
        return currentInQueue;
    }

    public void setCurrentInQueue(int currentInQueue) {
        this.currentInQueue = currentInQueue;
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
