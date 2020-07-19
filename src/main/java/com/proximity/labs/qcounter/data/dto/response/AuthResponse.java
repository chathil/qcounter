package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.user.AccountType;

import org.springframework.beans.factory.annotation.Value;

public class AuthResponse {
    private long id;
    @JsonProperty("hex_id")
    private String hexId; 
    @JsonProperty("device_token")
    private String deviceToken;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType = "Bearer ";
    @JsonProperty("expiry_duration")
    private long expiryDuration;
    @JsonProperty("ip_address")
    private String ipAddress;
    private String name;
    private String email;
    @JsonProperty("profile_picture")
    private String profilePicture;
    private String location;
    @JsonProperty("account_type")
    private AccountType accountType;
    @JsonProperty("profile_completion")
    private int profileCompletion;
    @JsonProperty("next_queue")
    private String nextQueue = null;
    @JsonProperty("your_queue")
    private String yourQueue = null;

    public AuthResponse(long id, String hexId, String deviceToken, String refreshToken, String accessToken, long expiryDuration, String ipAddress, String name, String email,
            String profilePicture, String location, AccountType accountType, int profileCompletion) {
        this.id = id;
        this.hexId = hexId;
        this.deviceToken = deviceToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDuration = expiryDuration;
        this.ipAddress = ipAddress;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
        this.location = location;
        this.accountType = accountType;
        this.profileCompletion = profileCompletion;
    }

    public long getId() {
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getHexId() {
        return hexId;
    }

    public void setHexId(String hexId) {
        this.hexId = hexId;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiryDuration() {
        return expiryDuration;
    }

    public void setExpiryDuration(long expiryDuration) {
        this.expiryDuration = expiryDuration;
    }

}