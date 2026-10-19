package com.exercise.backend.dto;

import java.time.LocalDate;

public class UserVisitResponse {
    private String username;
    private int currentStreak;
    private LocalDate lastVisitDate;

    public UserVisitResponse(String username, int currentStreak, LocalDate lastVisitDate) {
        this.username = username;
        this.currentStreak = currentStreak;
        this.lastVisitDate = lastVisitDate;
    }

    public String getUsername() {
        return username;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }
}
