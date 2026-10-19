package com.exercise.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.backend.dto.NutritionPlanResponseDTO;
import com.exercise.backend.service.NutritionPlanService;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins = "*")
public class NutritionPlanController {
    private final NutritionPlanService nutritionPlanService;

    public NutritionPlanController(NutritionPlanService nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @PostMapping("/calculate/{profileId}")
    public ResponseEntity<NutritionPlanResponseDTO> calculatePlan(@PathVariable Long profileId) {
        return ResponseEntity.ok(nutritionPlanService.calculateAndSavePlan(profileId));
    }
}
