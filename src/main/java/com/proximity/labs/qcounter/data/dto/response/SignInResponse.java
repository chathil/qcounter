package com.proximity.labs.qcounter.data.dto.response;

import com.proximity.labs.qcounter.data.models.user.AccountType;

public class SignInResponse extends AuthResponse {

    public SignInResponse(int id, String hexId, String deviceToken, String accessToken, String ipAddress, String name,
            String email, String profilePicture, String location, AccountType accountType, int profileCompletion) {
        super(id, hexId, deviceToken, accessToken, ipAddress, name, email, profilePicture, location, accountType,
                profileCompletion);
        // TODO Auto-generated constructor stub
    }

    
}