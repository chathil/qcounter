package com.proximity.labs.qcounter.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;

public class QrCodeRequest {

    @JsonProperty("queue_id")
    private String queueId;

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }
    
}