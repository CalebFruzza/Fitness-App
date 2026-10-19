package com.exercise.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.backend.dto.DailyLogDTO;
import com.exercise.backend.model.DailyLog;
import com.exercise.backend.service.DailyLogService;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class DailyLogController {
    private final DailyLogService dailyLogService;

    public DailyLogController(DailyLogService dailyLogService) {
        this.dailyLogService = dailyLogService;
    }

    @PostMapping("/{profileId}")
    public ResponseEntity<DailyLog> saveLog(@PathVariable Long profileId, @RequestBody DailyLogDTO dto) {
        return ResponseEntity.ok(dailyLogService.saveLog(profileId, dto));
    }

    @GetMapping("/{profileId}/{date}")
    public ResponseEntity<DailyLog> getLog(@PathVariable Long profileId, @PathVariable String date) {
        return ResponseEntity.ok(dailyLogService.getLog(profileId, date));
    }
}
