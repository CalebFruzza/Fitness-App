package com.exercise.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.backend.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByDateBetween(String startDate, String endDate);
}
