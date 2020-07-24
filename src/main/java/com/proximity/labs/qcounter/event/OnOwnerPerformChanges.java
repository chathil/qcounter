package com.proximity.labs.qcounter.event;

import org.springframework.context.ApplicationEvent;

public class OnOwnerPerformChanges extends ApplicationEvent {

    private String queueId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param operation the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public OnOwnerPerformChanges(String operation, String queueId) {
        super(operation);
        this.queueId = queueId;
    }

    public String getQueueId() {
        return queueId;
    }

}
