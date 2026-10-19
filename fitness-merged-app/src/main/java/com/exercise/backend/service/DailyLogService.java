package com.exercise.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exercise.backend.dto.DailyLogDTO;
import com.exercise.backend.model.DailyLog;
import com.exercise.backend.model.UserProfile;
import com.exercise.backend.repository.DailyLogRepository;

@Service
public class DailyLogService {
    private final DailyLogRepository dailyLogRepository;
    private final UserProfileService userProfileService;

    public DailyLogService(DailyLogRepository dailyLogRepository, UserProfileService userProfileService) {
        this.dailyLogRepository = dailyLogRepository;
        this.userProfileService = userProfileService;
    }

    public DailyLog saveLog(Long profileId, DailyLogDTO dto) {
        UserProfile profile = userProfileService.getProfile(profileId);

        int breakfast = dto.breakfast != null ? dto.breakfast : 0;
        int lunch = dto.lunch != null ? dto.lunch : 0;
        int dinner = dto.dinner != null ? dto.dinner : 0;
        int snacks = dto.snacks != null ? dto.snacks : 0;
        int total = breakfast + lunch + dinner + snacks;
        LocalDate date = LocalDate.parse(dto.date);

        DailyLog log = dailyLogRepository.findByProfileIdAndLogDate(profileId, date).orElse(new DailyLog());
        log.setProfile(profile);
        log.setLogDate(date);
        log.setBreakfastKcal(breakfast);
        log.setLunchKcal(lunch);
        log.setDinnerKcal(dinner);
        log.setSnacksKcal(snacks);
        log.setTotalKcal(total);
        return dailyLogRepository.save(log);
    }

    public DailyLog getLog(Long profileId, String date) {
        return dailyLogRepository.findByProfileIdAndLogDate(profileId, LocalDate.parse(date))
            .orElseThrow(() -> new RuntimeException("Log not found"));
    }

    public List<DailyLog> getLogsBetween(Long profileId, LocalDate startDate, LocalDate endDate) {
        return dailyLogRepository.findByProfileIdAndLogDateBetween(profileId, startDate, endDate);
    }
}
