package com.exercise.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.backend.model.Exercise;
import com.exercise.backend.service.ExerciseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<Exercise> getExercises() {
        return exerciseService.getAll();
    }

    @PostMapping
    public ResponseEntity<Exercise> addExercise(@Valid @RequestBody Exercise exercise) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseService.add(exercise));
    }

    @PutMapping("/{id}")
    public Exercise updateExercise(@PathVariable Long id, @Valid @RequestBody Exercise exercise) {
        return exerciseService.update(id, exercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
