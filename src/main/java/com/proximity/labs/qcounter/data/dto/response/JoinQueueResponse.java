package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.queue.QueueState;

public class JoinQueueResponse {
    private String id;
    @JsonProperty("queue_name")
    private String queueName;
    @JsonProperty("owner_name")
    private String ownerName;
    @JsonProperty("owner_id")
    private Long ownerId;
    @JsonProperty("queue_desc")
    private String queueDesc;
    @JsonProperty("queue_contact")
    private String queueContact;
    @JsonProperty("queue_max")
    private int queueMax;
    @JsonProperty("queue_current_num")
    private int queueCurrentNum;
    @JsonProperty("client_num")
    private int clientNum;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("client_contact")
    private String clientContact;
    @JsonProperty("increment_by")
    private int incrementBy;
    @JsonProperty("valid_until")
    private long validUntil;
    @JsonProperty("is_closed_queue")
    private boolean isClosedQueue;
    @JsonProperty("queue_state")
    private QueueState queueState;

    public JoinQueueResponse(String id, String queueName, String queueDesc, String queueContact, int queueMax, int queueCurrentNum,
            int incrementBy, long validUntil, boolean isClosedQueue, QueueState queueState, int clientNum, String clientName,
            String clientContact, String ownerName, Long ownerId) {
        this.id = id;
        this.queueName = queueName;
        this.queueDesc = queueDesc;
        this.queueContact = queueContact;
        this.queueMax = queueMax;
        this.queueCurrentNum = queueCurrentNum;
        this.incrementBy = incrementBy;
        this.validUntil = validUntil;
        this.isClosedQueue = isClosedQueue;
        this.queueState = queueState;
        this.clientNum = clientNum;
        this.clientName= clientName;
        this.clientContact = clientContact;
        this.ownerName = ownerName;
        this.ownerId= ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueDesc() {
        return queueDesc;
    }

    public void setQueueDesc(String queueDesc) {
        this.queueDesc = queueDesc;
    }

    public String getQueueContact() {
        return queueContact;
    }

    public void setQueueContact(String queueContact) {
        this.queueContact = queueContact;
    }

    public int getQueueMax() {
        return queueMax;
    }

    public void setQueueMax(int queueMax) {
        this.queueMax = queueMax;
    }

    public int getQueueCurrentNum() {
        return queueCurrentNum;
    }

    public void setQueueCurrentNum(int queueCurrentNum) {
        this.queueCurrentNum = queueCurrentNum;
    }

    public int getClientNum() {
        return clientNum;
    }

    public void setClientNum(int clientNum) {
        this.clientNum = clientNum;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public int getIncrementBy() {
        return incrementBy;
    }

    public void setIncrementBy(int incrementBy) {
        this.incrementBy = incrementBy;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

    public boolean isClosedQueue() {
        return isClosedQueue;
    }

    public void setClosedQueue(boolean isClosedQueue) {
        this.isClosedQueue = isClosedQueue;
    }

    public QueueState getQueueState() {
        return queueState;
    }

    public void setQueueState(QueueState queueState) {
        this.queueState = queueState;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}