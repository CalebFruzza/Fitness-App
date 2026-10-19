package com.exercise.backend.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.exercise.backend.dto.UserVisitResponse;

@Service
public class UserVisitService {
    private final Map<String, UserVisitResponse> visits = new HashMap<>();

    public synchronized UserVisitResponse recordVisit(String username) {
        LocalDate today = LocalDate.now();
        UserVisitResponse existing = visits.get(username);
        int streak = 1;

        if (existing != null) {
            LocalDate lastVisit = existing.getLastVisitDate();
            if (today.equals(lastVisit)) {
                streak = existing.getCurrentStreak();
            } else if (today.minusDays(1).equals(lastVisit)) {
                streak = existing.getCurrentStreak() + 1;
            }
        }

        UserVisitResponse updated = new UserVisitResponse(username, streak, today);
        visits.put(username, updated);
        return updated;
    }
}
