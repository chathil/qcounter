package com.proximity.labs.qcounter.data.dto.response;

import com.proximity.labs.qcounter.data.models.user.AccountType;

public abstract class AuthResponse {
    private int id;
    private String hexId; 
    private String deviceToken;
    private String accessToken;
    private String ipAddress;
    private String name;
    private String email;
    private String profilePicture;
    private String location;
    private AccountType accountType;
    private int profileCompletion;
    private String nextQueue = null;
    private String yourQueue = null;

    public AuthResponse(int id, String hexId, String deviceToken, String accessToken, String ipAddress, String name, String email,
            String profilePicture, String location, AccountType accountType, int profileCompletion) {
        this.id = id;
        this.hexId = hexId;
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