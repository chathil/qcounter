package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

import java.util.ArrayList;
import java.util.List;

public class ControlCounterResponse {

    private final String id;
    private final List<InQueue> next = new ArrayList<>();
    private final List<InQueue> prev = new ArrayList<>();
    private final List<InQueue> current = new ArrayList<>();
    @JsonProperty("queue_state")
    private final QueueState queueState;
    @JsonProperty("current_num")
    private final int currentNum;
    private final int of;
    private final int max;

    public ControlCounterResponse(String id, QueueState queueState, int currentNum, int of, int max) {
        this.id = id;
        this.queueState = queueState;
        this.currentNum = currentNum;
        this.of = of;
        this.max = max;
    }

    public void addNext(long userId, String fullName) {
        next.add(new InQueue(userId, fullName));
    }

    public void addPrev(long userId, String fullName) {
        prev.add(new InQueue(userId, fullName));
    }

    public void addCurrent(long userId, String fullName) {
        current.add(new InQueue(userId, fullName));
    }

    public List<InQueue> getCurrent() {
        return current;
    }

    public String getId() {
        return id;
    }

    public List<InQueue> getNext() {
        return next;
    }

    public List<InQueue> getPrev() {
        return prev;
    }

    public QueueState getQueueState() {
        return queueState;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public int getOf() {
        return of;
    }

    public int getMax() {
        return max;
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

    public static final class InQueue {
        private final long id;
        private final String name;

        public InQueue(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
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

}


