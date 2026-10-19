package com.exercise.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exercise.backend.dto.DashboardSummaryResponse;
import com.exercise.backend.model.DailyLog;
import com.exercise.backend.model.UserProfile;

@Service
public class DashboardService {
    private static final DateTimeFormatter LABEL_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final UserProfileService userProfileService;
    private final DailyLogService dailyLogService;
    private final NutritionPlanService nutritionPlanService;
    private final ExerciseService exerciseService;

    public DashboardService(UserProfileService userProfileService, DailyLogService dailyLogService, NutritionPlanService nutritionPlanService, ExerciseService exerciseService) {
        this.userProfileService = userProfileService;
        this.dailyLogService = dailyLogService;
        this.nutritionPlanService = nutritionPlanService;
        this.exerciseService = exerciseService;
    }

    public DashboardSummaryResponse getSummary(String range) {
        UserProfile profile = userProfileService.getLatestProfile();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = resolveStartDate(range, endDate);
        List<DailyLog> logs = dailyLogService.getLogsBetween(profile.getId(), startDate, endDate);
        int caloriesIn = logs.stream().map(DailyLog::getTotalKcal).filter(v -> v != null).mapToInt(Integer::intValue).sum();
        int caloriesOut = exerciseService.getCaloriesBetween(startDate.toString(), endDate.toString());
        int goal = nutritionPlanService.getTargetCaloriesForProfile(profile.getId());
        return new DashboardSummaryResponse(buildLabel(startDate, endDate), caloriesIn, caloriesOut, goal);
    }

    private LocalDate resolveStartDate(String range, LocalDate endDate) {
        if (range == null) {
            return endDate;
        }
        return switch (range) {
            case "lastWeek" -> endDate.minusDays(6);
            case "lastMonth" -> endDate.minusMonths(1).withDayOfMonth(1);
            case "twoMonths" -> endDate.minusMonths(2).withDayOfMonth(1);
            case "threeMonths" -> endDate.minusMonths(3).withDayOfMonth(1);
            default -> endDate;
        };
    }

    private String buildLabel(LocalDate startDate, LocalDate endDate) {
        if (startDate.equals(endDate)) {
            return endDate.format(LABEL_FORMAT);
        }
        return startDate.format(LABEL_FORMAT) + " - " + endDate.format(LABEL_FORMAT);
    }
}
