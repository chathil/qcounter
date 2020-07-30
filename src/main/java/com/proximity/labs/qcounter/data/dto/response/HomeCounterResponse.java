package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

import java.util.ArrayList;
import java.util.List;

public class HomeCounterResponse {
    private NextQueueCounter nextQueueCounter;
    private final List<MyQueue> myQueues = new ArrayList<>();

    public NextQueueCounter getNextQueueCounter() {
        return nextQueueCounter;
    }

    public void setNextQueueCounter(String id, int myNumber, int currentNum, QueueState state) {
        this.nextQueueCounter = new NextQueueCounter(id, myNumber, currentNum, state);
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
        private final int myNumber;
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
