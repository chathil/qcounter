package com.proximity.labs.qcounter.utils;

public enum CounterSocketAtrr {

    HOME("home"),
    JOIN("join"),
    CONTROL("control"),
    QUEUE_ID("queue_id"),
    TARGET_PATH("target_path"),
    OPERATION("operation"),
    INCREMENT("increment"),
    DECREMENT("decrement");

    public final String label;

    CounterSocketAtrr(String label) {
        this.label = label;
    }
}
