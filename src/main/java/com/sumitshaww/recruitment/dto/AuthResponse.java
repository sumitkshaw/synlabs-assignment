package com.sumitshaww.recruitment.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String message;
    private String userType;

    public AuthResponse(String token, String message, String userType) {
        this.token = token;
        this.message = message;
        this.userType = userType;
    }
}