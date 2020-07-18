package com.proximity.labs.qcounter.data.dto.request;

public class GuestRequest implements Request {
    private String deviceToken;
    private String ipAddress;
    private String name;

    public GuestRequest(String deviceToken, String ipAddress, String name) {
        this.deviceToken = deviceToken;
        this.ipAddress = ipAddress;
        this.name = name;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }
}