package com.proximity.labs.qcounter.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel(value = "SignoutRequest Payload", description = "Request to log out a single device owned by a user")
public class SignoutRequest {
    @JsonProperty("device_token")
    private String deviceToken;

    public SignoutRequest(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public SignoutRequest() {
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
