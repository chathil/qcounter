package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.queue.QueueState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Queue Response", description = "Details about a queue")
public class QueueResponse {
    @ApiModelProperty(value = "queue id generated by client", required = true, allowableValues = "NonEmpty String", example = "KbFlP8A08y2k4Vp1XET", allowEmptyValue = false)
    private String id;
    @ApiModelProperty(value = "Name of the queue")
    private String name;
    @ApiModelProperty(value = "queue's owner")
    @JsonProperty("created_by")
    private String createdBy;
    @ApiModelProperty(value = "queue's owner id")
    @JsonProperty("creator_id")
    private long creatorId;
    @JsonProperty("queue_desc")
    private String desc;
    @ApiModelProperty(value = "max person in a queue")
    private int max;
    @ApiModelProperty(value = "how many users/ clients are served at once")
    @JsonProperty("incremented_by")
    private int incrementBy;
    @ApiModelProperty(value = "queue's lifetime")
    @JsonProperty("valid_until")
    private Long validUntil;
    @ApiModelProperty(value = "queue's contact")
    private String contact;
    @ApiModelProperty(value = "queue can only be joined by id, or search-able")
    @JsonProperty("is_closed_queue")
    private boolean isClosedQueue;
    @ApiModelProperty(value = "state of a queue in enum")
    private QueueState state;
    @ApiModelProperty(value = "current queue number")
    @JsonProperty("current_queue")
    private final int currentQueue;
    @JsonProperty("current_in_queue")
    @ApiModelProperty(value = "count of users/ clients currently in a queue")
    private final int currentInQueue;
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

    public int getCurrentQueue() {
        return currentQueue;
    }

    public int getCurrentInQueue() {
        return currentInQueue;
    }

    public QueueResponse(String id, String name, String createdBy, long creatorId, String desc, int max,
                         int incrementBy, Long validUntil, String contact, boolean isClosedQueue, QueueState state, int currentQueue, int currentInQueue, String qrCode) {
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
        this.currentInQueue = currentInQueue;
        this.currentQueue = currentQueue;
    }
}