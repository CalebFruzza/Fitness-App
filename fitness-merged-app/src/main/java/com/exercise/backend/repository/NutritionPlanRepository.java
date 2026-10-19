package com.exercise.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.backend.model.NutritionPlan;

public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    NutritionPlan findTopByProfileIdOrderByIdDesc(Long profileId);
}
