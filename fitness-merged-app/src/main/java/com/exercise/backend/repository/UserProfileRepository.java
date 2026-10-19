package com.exercise.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.backend.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findTopByOrderByIdDesc();
}
