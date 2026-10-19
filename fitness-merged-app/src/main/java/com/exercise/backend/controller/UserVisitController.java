package com.exercise.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.backend.dto.UserVisitResponse;
import com.exercise.backend.service.UserVisitService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserVisitController {
    private final UserVisitService userVisitService;

    public UserVisitController(UserVisitService userVisitService) {
        this.userVisitService = userVisitService;
    }

    @PostMapping("/{username}/visit")
    public UserVisitResponse recordVisit(@PathVariable String username) {
        return userVisitService.recordVisit(username);
    }
}
