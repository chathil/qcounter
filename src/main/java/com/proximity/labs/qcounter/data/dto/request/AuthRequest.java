package com.proximity.labs.qcounter.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public abstract class AuthRequest implements Request {

    @ApiModelProperty(value = "device token generated by firebase on client, this token is unique to each device and can be use to send notification via FCM.", required = true, allowableValues = "NonEmpty String")
    @JsonProperty("device_token")
    private String deviceToken;

    @ApiModelProperty(value = "IP address send from client at signup/ signup, this will be use to get the user creator location. Location is needed to make sure a user that want to join a queue can only join at a reasonable radius", required = true, allowableValues = "NonEmpty String")
    @JsonProperty("ip_address")
    private String ipAddress;

    @ApiModelProperty(value = "User's email. this is a natural id", required = true, allowableValues = "NonEmpty String")
    private String email;

    @ApiModelProperty(value = "User's password", required = true, allowableValues = "NonEmpty String")
    private String password;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}