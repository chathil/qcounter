package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

public class NewQueueResponse {
    
    private String id;
    private String name;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("creator_id")
    private long creatorId;
    private String desc;
    private int max;
    @JsonProperty("incremented_by")
    private int incrementBy;
    @JsonProperty("valid_until")
    private Long validUntil;
    private String contact;
    @JsonProperty("is_closed_queue")
    private boolean isClosedQueue;
    private QueueState state;
    @JsonProperty("qr_code")
    private String qrCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getIncrementBy() {
        return incrementBy;
    }

    public void setIncrementBy(int incrementBy) {
        this.incrementBy = incrementBy;
    }

    public Long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Long validUntil) {
        this.validUntil = validUntil;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isClosedQueue() {
        return isClosedQueue;
    }

    public void setClosedQueue(boolean isClosedQueue) {
        this.isClosedQueue = isClosedQueue;
    }

    public QueueState getState() {
        return state;
    }

    public void setState(QueueState state) {
        this.state = state;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public NewQueueResponse(String id, String name, String createdBy, long creatorId, String desc, int max,
            int incrementBy, Long validUntil, String contact, boolean isClosedQueue, QueueState state, String qrCode) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.creatorId = creatorId;
        this.desc = desc;
        this.max = max;
        this.incrementBy = incrementBy;
        this.validUntil = validUntil;
        this.contact = contact;
        this.isClosedQueue = isClosedQueue;
        this.state = state;
        this.qrCode = qrCode;
    }
}