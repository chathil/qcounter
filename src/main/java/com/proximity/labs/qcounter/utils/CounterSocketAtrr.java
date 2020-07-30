package com.proximity.labs.qcounter.utils;

public enum CounterSocketAtrr {
    TARGET_PATH("target_path"),
    HOME("home"),
    JOIN("join"),
    CONTROL("control"),

    QUEUE_ID("queue_id"),

    OPERATION("operation"),

    INCREMENT("increment"),
    DECREMENT("decrement"),
    PAUSE("pause"),
    RESUME("resume");

    public final String label;

    CounterSocketAtrr(String label) {
        this.label = label;
    }
}
