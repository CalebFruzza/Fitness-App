package com.exercise.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.backend.model.DailyLog;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    Optional<DailyLog> findByProfileIdAndLogDate(Long profileId, LocalDate logDate);
    List<DailyLog> findByProfileIdAndLogDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}
