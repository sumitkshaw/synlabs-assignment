package com.sumitshaww.recruitment.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String userType; // "ADMIN" or "APPLICANT"
    private String address;
    private String profileHeadline;
}