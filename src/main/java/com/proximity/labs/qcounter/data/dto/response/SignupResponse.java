package com.proximity.labs.qcounter.data.dto.response;

import com.proximity.labs.qcounter.data.models.user.AccountType;

public class SignupResponse implements Response {
    private final int id;
    private final String deviceToken;
    private final String accessToken;
    private final String ipAddress;
    private final String name;
    private final String email;
    private final String profilePicture;
    private final String location;
    private final AccountType accountType;
    private final int profileCompletion;
    private final String nextQueue = null;
    private final String yourQueue = null;

    public SignupResponse(int id, String deviceToken, String accessToken, String ipAddress, String name, String email,
            String profilePicture, String location, AccountType accountType, int profileCompletion) {
        this.id = id;
        this.deviceToken = deviceToken;
        this.accessToken = accessToken;
        this.ipAddress = ipAddress;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
        this.location = location;
        this.accountType = accountType;
        this.profileCompletion = profileCompletion;
    }

    String hexId() {
        return Integer.toHexString(id);
    }

    public int getId() {
        return id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getLocation() {
        return location;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public int getProfileCompletion() {
        return profileCompletion;
    }

    public String getNextQueue() {
        return nextQueue;
    }

    public String getYourQueue() {
        return yourQueue;
    }

}