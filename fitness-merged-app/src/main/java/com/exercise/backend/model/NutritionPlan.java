package com.exercise.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class NutritionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maintenanceKcal;
    private Integer targetKcal;
    private Integer proteinPct;
    private Integer carbPct;
    private Integer fatPct;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    public NutritionPlan() {
    }

    public NutritionPlan(Integer maintenanceKcal, Integer targetKcal, Integer proteinPct, Integer carbPct, Integer fatPct, UserProfile profile) {
        this.maintenanceKcal = maintenanceKcal;
        this.targetKcal = targetKcal;
        this.proteinPct = proteinPct;
        this.carbPct = carbPct;
        this.fatPct = fatPct;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public Integer getMaintenanceKcal() {
        return maintenanceKcal;
    }

    public Integer getTargetKcal() {
        return targetKcal;
    }

    public Integer getProteinPct() {
        return proteinPct;
    }

    public Integer getCarbPct() {
        return carbPct;
    }

    public Integer getFatPct() {
        return fatPct;
    }

    public UserProfile getProfile() {
        return profile;
    }
}
