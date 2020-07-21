package com.proximity.labs.qcounter.data.models.queue;

public enum QueueState {
    RUNNING("running"),
    PAUSED("paused"),
    EXPIRED("expired"),
    DELETED("deleted");

    public final String label;
   
    private QueueState(String label) {
        this.label = label;
    }
}