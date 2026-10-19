package com.exercise.backend.dto;

public class DashboardSummaryResponse {
    private final String label;
    private final int caloriesIn;
    private final int caloriesOut;
    private final int goal;
    private final int netCalories;
    private final int caloriesLeft;
    private final int progressPercent;

    public DashboardSummaryResponse(String label, int caloriesIn, int caloriesOut, int goal) {
        this.label = label;
        this.caloriesIn = caloriesIn;
        this.caloriesOut = caloriesOut;
        this.goal = goal;
        this.netCalories = caloriesIn - caloriesOut;
        this.caloriesLeft = goal - this.netCalories;
        this.progressPercent = goal > 0 ? Math.max(0, Math.min(100, (int) Math.round((this.netCalories * 100.0) / goal))) : 0;
    }

    public String getLabel() {
        return label;
    }

    public int getCaloriesIn() {
        return caloriesIn;
    }

    public int getCaloriesOut() {
        return caloriesOut;
    }

    public int getGoal() {
        return goal;
    }

    public int getNetCalories() {
        return netCalories;
    }

    public int getCaloriesLeft() {
        return caloriesLeft;
    }

    public int getProgressPercent() {
        return progressPercent;
    }
}
