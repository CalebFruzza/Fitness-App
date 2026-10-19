package com.exercise.backend.service;

import org.springframework.stereotype.Service;

import com.exercise.backend.dto.NutritionPlanResponseDTO;
import com.exercise.backend.model.NutritionPlan;
import com.exercise.backend.model.UserProfile;
import com.exercise.backend.repository.NutritionPlanRepository;

@Service
public class NutritionPlanService {
    private final NutritionPlanRepository nutritionPlanRepository;
    private final UserProfileService userProfileService;

    public NutritionPlanService(NutritionPlanRepository nutritionPlanRepository, UserProfileService userProfileService) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.userProfileService = userProfileService;
    }

    public NutritionPlanResponseDTO calculateAndSavePlan(Long profileId) {
        UserProfile profile = userProfileService.getProfile(profileId);

        double bmr;
        if ("female".equalsIgnoreCase(profile.getSex())) {
            bmr = 10.0 * profile.getWeightKg() + 6.25 * profile.getHeightCm() - 5.0 * profile.getAge() - 161.0;
        } else {
            bmr = 10.0 * profile.getWeightKg() + 6.25 * profile.getHeightCm() - 5.0 * profile.getAge() + 5.0;
        }

        int maintenance = (int) Math.round(bmr * profile.getActivityLevel());
        int target = maintenance;

        if ("mild-loss".equals(profile.getGoal())) {
            target -= 250;
        } else if ("mild-gain".equals(profile.getGoal())) {
            target += 250;
        }

        int proteinPct = 20;
        int carbPct = 50;
        int fatPct = 30;

        NutritionPlan plan = new NutritionPlan(maintenance, target, proteinPct, carbPct, fatPct, profile);
        nutritionPlanRepository.save(plan);

        NutritionPlanResponseDTO response = new NutritionPlanResponseDTO();
        response.profileId = profileId;
        response.maintenanceCalories = maintenance;
        response.targetCalories = target;
        response.proteinPct = proteinPct;
        response.carbPct = carbPct;
        response.fatPct = fatPct;
        response.proteinGrams = (int) Math.round((target * proteinPct) / 100.0 / 4.0);
        response.carbGrams = (int) Math.round((target * carbPct) / 100.0 / 4.0);
        response.fatGrams = (int) Math.round((target * fatPct) / 100.0 / 9.0);
        return response;
    }

    public int getTargetCaloriesForProfile(Long profileId) {
        NutritionPlan latest = nutritionPlanRepository.findTopByProfileIdOrderByIdDesc(profileId);
        if (latest != null) {
            return latest.getTargetKcal();
        }
        return calculateAndSavePlan(profileId).targetCalories;
    }
}
