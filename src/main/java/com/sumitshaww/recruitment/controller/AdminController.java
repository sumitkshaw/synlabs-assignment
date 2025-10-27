package com.sumitshaww.recruitment.controller;

import com.sumitshaww.recruitment.entity.Profile;
import com.sumitshaww.recruitment.entity.User;
import com.sumitshaww.recruitment.repository.ProfileRepository;
import com.sumitshaww.recruitment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @GetMapping("/applicants")
    public ResponseEntity<List<User>> getAllApplicants() {
        List<User> applicants = userRepository.findAll().stream()
                .filter(user -> "APPLICANT".equals(user.getUserType()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicants);
    }

    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<Profile> getApplicantProfile(@PathVariable Long applicantId) {
        Profile profile = profileRepository.findByUserId(applicantId)
                .orElseThrow(() -> new RuntimeException("Profile not found for applicant"));
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}