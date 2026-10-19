package com.exercise.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.backend.dto.UserProfileDTO;
import com.exercise.backend.model.UserProfile;
import com.exercise.backend.service.UserProfileService;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfile> saveProfile(@RequestBody UserProfileDTO dto) {
        return ResponseEntity.ok(userProfileService.saveProfile(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getProfile(id));
    }
}
