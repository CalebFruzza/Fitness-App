package com.exercise.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "supplements")
public class Supplement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than 0")
    private Long userId;

    @Column(nullable = false)
    @NotBlank(message = "Supplement name is required")
    @Size(max = 100, message = "Supplement name must be 100 characters or fewer")
    private String supplementName;

    @Column(nullable = false)
    @NotBlank(message = "Dosage is required")
    @Size(max = 50, message = "Dosage must be 50 characters or fewer")
    private String dosage;

    @Column(nullable = false)
    @NotNull(message = "Date taken is required")
    @PastOrPresent(message = "Date taken cannot be in the future")
    private LocalDate dateTaken;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSupplementName() {
        return supplementName;
    }

    public void setSupplementName(String supplementName) {
        this.supplementName = supplementName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public LocalDate getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(LocalDate dateTaken) {
        this.dateTaken = dateTaken;
    }
}
