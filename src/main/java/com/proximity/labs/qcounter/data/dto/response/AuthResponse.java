package com.proximity.labs.qcounter.data.dto.response;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.models.role.Role;

import org.springframework.beans.factory.annotation.Value;

public class AuthResponse {
    private final long id;
    @JsonProperty("hex_id")
    private String hexId; 
    @JsonProperty("device_token")
    private final String deviceToken;
    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType = "Bearer ";
    @JsonProperty("expiry_duration")
    private long expiryDuration;
    @JsonProperty("ip_address")
    private final String ipAddress;
    private final Set<Role> roles;
    private final String name;
    private final String email;
    @JsonProperty("profile_picture")
    private final String profilePicture;
    private final String location;
    @JsonProperty("profile_completion")
    private final int profileCompletion;
    @JsonProperty("next_queue")
    private final String nextQueue = null;
    @JsonProperty("your_queue")
    private final String yourQueue = null;

    public AuthResponse(long id, String hexId, String deviceToken, String refreshToken, String accessToken, long expiryDuration, String ipAddress, Set<Role> roles, String name, String email,
            String profilePicture, String location, int profileCompletion) {
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
        this.profileCompletion = profileCompletion;
        this.roles = roles;
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

    public Set<Role> getRoles() {
        return roles;
    }
}