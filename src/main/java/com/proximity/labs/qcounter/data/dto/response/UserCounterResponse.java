package com.proximity.labs.qcounter.data.dto.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

public class UserCounterResponse {

    @JsonProperty("my_queues")
    private ArrayList<MyQueue> myQueues;
    @JsonProperty("next_queue_counter")
    private NextQueueCounter nextQueueCounter;

    public class NextQueueCounter {
        @JsonProperty("current_num")
        private int currentNum;
        @JsonProperty("client_num")
        private int clientNum;
        @JsonProperty("queue_state")
        private QueueState queueState;
        
        public int getCurrentNum() {
            return currentNum;
        }

        public void setCurrentNum(int currentNum) {
            this.currentNum = currentNum;
        }

        public int getClientNum() {
            return clientNum;
        }

        public void setClientNum(int clientNum) {
            this.clientNum = clientNum;
        }

        public QueueState getQueueState() {
            return queueState;
        }

        public void setQueueState(QueueState queueState) {
            this.queueState = queueState;
        }

        public NextQueueCounter(int currentNum, int clientNum, QueueState queueState) {
            this.currentNum = currentNum;
            this.clientNum = clientNum;
            this.queueState = queueState;
        }
        
    }

    public class MyQueue {
        private int id;
        private String name;
        @JsonProperty("increment_by")
        private int incrementBy;
        private int current;
        private int of;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIncrementBy() {
            return incrementBy;
        }

        public void setIncrementBy(int incrementBy) {
            this.incrementBy = incrementBy;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getOf() {
            return of;
        }

        public void setOf(int of) {
            this.of = of;
        }

        public MyQueue(int id, String name, int incrementBy, int current, int of) {
            this.id = id;
            this.name = name;
            this.incrementBy = incrementBy;
            this.current = current;
            this.of = of;
        }
    }

    public ArrayList<MyQueue> getMyQueues() {
        return myQueues;
    }

    public void setMyQueues(ArrayList<MyQueue> myQueues) {
        this.myQueues = myQueues;
    }

    public NextQueueCounter getNextQueueCounter() {
        return nextQueueCounter;
    }

    public void setNextQueueCounter(NextQueueCounter nextQueueCounter) {
        this.nextQueueCounter = nextQueueCounter;
    }

    public UserCounterResponse(NextQueueCounter nextQueueCounter) {
        this.myQueues = new ArrayList<MyQueue>();
        this.nextQueueCounter = nextQueueCounter;
    }
}