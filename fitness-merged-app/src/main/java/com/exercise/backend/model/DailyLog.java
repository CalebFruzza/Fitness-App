package com.exercise.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate logDate;
    private Integer breakfastKcal;
    private Integer lunchKcal;
    private Integer dinnerKcal;
    private Integer snacksKcal;
    private Integer totalKcal;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    public Long getId() {
        return id;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Integer getBreakfastKcal() {
        return breakfastKcal;
    }

    public void setBreakfastKcal(Integer breakfastKcal) {
        this.breakfastKcal = breakfastKcal;
    }

    public Integer getLunchKcal() {
        return lunchKcal;
    }

    public void setLunchKcal(Integer lunchKcal) {
        this.lunchKcal = lunchKcal;
    }

    public Integer getDinnerKcal() {
        return dinnerKcal;
    }

    public void setDinnerKcal(Integer dinnerKcal) {
        this.dinnerKcal = dinnerKcal;
    }

    public Integer getSnacksKcal() {
        return snacksKcal;
    }

    public void setSnacksKcal(Integer snacksKcal) {
        this.snacksKcal = snacksKcal;
    }

    public Integer getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(Integer totalKcal) {
        this.totalKcal = totalKcal;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
