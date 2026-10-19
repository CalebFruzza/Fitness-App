package com.exercise.backend.service;

import org.springframework.stereotype.Service;

import com.exercise.backend.dto.UserProfileDTO;
import com.exercise.backend.model.UserProfile;
import com.exercise.backend.repository.UserProfileRepository;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile saveProfile(UserProfileDTO dto) {
        UserProfile profile = new UserProfile(dto.age, dto.sex, dto.height, dto.weight, dto.activity, dto.goal);
        return userProfileRepository.save(profile);
    }

    public UserProfile getProfile(Long id) {
        return userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public UserProfile getLatestProfile() {
        UserProfile profile = userProfileRepository.findTopByOrderByIdDesc();
        if (profile != null) {
            return profile;
        }

        UserProfile fallback = new UserProfile(25, "male", 175, 70.0, 1.55, "maintain");
        return userProfileRepository.save(fallback);
    }
}
