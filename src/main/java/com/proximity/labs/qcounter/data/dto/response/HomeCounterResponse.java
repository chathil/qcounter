package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

import java.util.ArrayList;
import java.util.List;

public class HomeCounterResponse {
    @JsonProperty("next_queue_counter")
    private NextQueueCounter next;
    @JsonProperty("my_queues")
    private final List<MyQueue> myQueues = new ArrayList<>();

    public NextQueueCounter getNext() {
        return next;
    }

    public void setNextQueueCounter(String id, int myNumber, int currentNum, QueueState state) {
        this.next = new NextQueueCounter(id, myNumber, currentNum, state);
    }

    public List<MyQueue> getMyQueues() {
        return myQueues;
    }

    public void setMyQueues(String id, String name, int incrementBy, int current, int of) {
        this.myQueues.add(new MyQueue(id, name, incrementBy, current, of));
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

    public static final class MyQueue {
        private final String id;
        private final String name;
        @JsonProperty("increment_by")
        private final int incrementBy;
        private final int current;
        private final int of;

        public MyQueue(String id, String name, int incrementBy, int current, int of) {
            this.id = id;
            this.name = name;
            this.incrementBy = incrementBy;
            this.current = current;
            this.of = of;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getIncrementBy() {
            return incrementBy;
        }

        public int getCurrent() {
            return current;
        }

        public int getOf() {
            return of;
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

    public static final class NextQueueCounter {
        private final String id;
        @JsonProperty("my_number")
        private final int myNumber;
        @JsonProperty("current_num")
        private final int currentNum;
        private final QueueState state;

        public NextQueueCounter(String id, int myNumber, int currentNum, QueueState state) {
            this.id = id;
            this.myNumber = myNumber;
            this.currentNum = currentNum;
            this.state = state;
        }

        public String getId() {
            return id;
        }

        public int getMyNumber() {
            return myNumber;
        }

        public QueueState getState() {
            return state;
        }

        public int getCurrentNum() {
            return currentNum;
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
