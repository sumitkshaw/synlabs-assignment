package com.sumitshaww.recruitment.controller;

import com.sumitshaww.recruitment.entity.Application;
import com.sumitshaww.recruitment.entity.Profile;
import com.sumitshaww.recruitment.entity.User;
import com.sumitshaww.recruitment.repository.ProfileRepository;
import com.sumitshaww.recruitment.service.ApplicationService;
import com.sumitshaww.recruitment.service.FileStorageService;
import com.sumitshaww.recruitment.service.ResumeParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final FileStorageService fileStorageService;
    private final ResumeParserService resumeParserService;
    private final ProfileRepository profileRepository;
    private final ApplicationService applicationService;

    @PostMapping("/uploadResume")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) {

        // Check if user is applicant
        if (!"APPLICANT".equals(user.getUserType())) {
            return ResponseEntity.badRequest().body("Only applicants can upload resumes");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (!"application/pdf".equals(contentType) &&
                !"application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType)) {
            return ResponseEntity.badRequest().body("Only PDF and DOCX files are allowed");
        }

        try {
            // Store file
            String fileName = fileStorageService.storeFile(file);

            // Parse resume using third-party API
            byte[] fileBytes = fileStorageService.loadFile(fileName);
            Map<String, Object> parsedData = resumeParserService.parseResume(fileBytes);

            // Create or update profile
            Profile profile = profileRepository.findByUserId(user.getId())
                    .orElse(new Profile());

            profile.setUser(user);
            profile.setResumeFilePath(fileName);
            profile.setSkills(convertToString(parsedData.get("skills")));
            profile.setEducation(convertToString(parsedData.get("education")));
            profile.setExperience(convertToString(parsedData.get("experience")));
            profile.setPhone((String) parsedData.get("phone"));
            profile.setExtractedName((String) parsedData.get("name"));
            profile.setExtractedEmail((String) parsedData.get("email"));

            profileRepository.save(profile); // Removed unused variable

            return ResponseEntity.ok().body("Resume uploaded and parsed successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing resume: " + e.getMessage());
        }
    }

    @GetMapping("/jobs/apply")
    public ResponseEntity<?> applyForJob(
            @RequestParam Long jobId,
            @AuthenticationPrincipal User user) {

        if (!"APPLICANT".equals(user.getUserType())) {
            return ResponseEntity.badRequest().body("Only applicants can apply for jobs");
        }

        try {
            applicationService.applyForJob(jobId, user); // Removed unused variable
            return ResponseEntity.ok().body("Applied to job successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error applying to job: " + e.getMessage());
        }
    }

    private String convertToString(Object data) {
        if (data instanceof List) {
            List<?> list = (List<?>) data;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(list.get(i).toString());
            }
            return sb.toString();
        }
        return data != null ? data.toString() : null;
    }
}