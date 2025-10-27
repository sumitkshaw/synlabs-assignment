package com.sumitshaww.recruitment.controller;

import com.sumitshaww.recruitment.dto.AuthResponse;
import com.sumitshaww.recruitment.dto.LoginRequest;
import com.sumitshaww.recruitment.dto.SignupRequest;
import com.sumitshaww.recruitment.entity.User;
import com.sumitshaww.recruitment.service.JwtService;
import com.sumitshaww.recruitment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        if (userService.emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Email already exists", null));
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUserType(request.getUserType().toUpperCase());
        user.setAddress(request.getAddress());
        user.setProfileHeadline(request.getProfileHeadline());

        User savedUser = userService.registerUser(user);
        String token = jwtService.generateToken(savedUser.getEmail());

        return ResponseEntity.ok(new AuthResponse(token, "User registered successfully", savedUser.getUserType()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Password validation will be handled by Spring Security
        // For now, we'll just generate token
        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(token, "Login successful", user.getUserType()));
    }
}