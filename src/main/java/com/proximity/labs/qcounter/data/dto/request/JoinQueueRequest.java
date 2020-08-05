package com.proximity.labs.qcounter.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinQueueRequest {

    @JsonProperty("queue_id")
    private String queueId;
    @JsonProperty("ip_address")
    private String ipAddress;
    @JsonProperty("full_name")
    private String fullName;
    private String contact;

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    

}