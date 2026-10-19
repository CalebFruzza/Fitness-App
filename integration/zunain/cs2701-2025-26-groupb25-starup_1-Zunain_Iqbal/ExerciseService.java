package com.example.fitnessapp.service;

import com.example.fitnessapp.model.Exercise;
import com.example.fitnessapp.repository.ExerciseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class ExerciseService {

    private static final Map<String, Double> MET_VALUES = Map.of(
        "running",       8.3,
        "cycling",       6.8,
        "swimming",      7.0,
        "walking",       3.8,
        "jumping-jacks", 10.0
    );

    // Standard reference weight used when none is provided (kg)
    private static final double REFERENCE_WEIGHT_KG = 70.0;

    private final ExerciseRepository repository;

    public ExerciseService(ExerciseRepository repository) {
        this.repository = repository;
    }

    public List<Exercise> getAll() {
        return repository.findAll();
    }

    public Exercise add(Exercise exercise) {
        validate(exercise);
        exercise.setCalories(calculateCalories(exercise.getName(), exercise.getDuration()));
        return repository.save(exercise);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found");
        }
        repository.deleteById(id);
    }

    public Exercise update(Long id, Exercise updated) {
        Exercise ex = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        validate(updated);
        ex.setName(updated.getName());
        ex.setDuration(updated.getDuration());
        ex.setDate(updated.getDate());
        ex.setCalories(calculateCalories(updated.getName(), updated.getDuration()));

        return repository.save(ex);
    }

    private int calculateCalories(String name, int duration) {
        double met = MET_VALUES.get(name);
        return (int) Math.round(met * REFERENCE_WEIGHT_KG * duration / 60.0);
    }

    private void validate(Exercise exercise) {
        if (exercise.getName() == null || !MET_VALUES.containsKey(exercise.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Invalid exercise name. Must be one of: " + MET_VALUES.keySet());
        }
        if (exercise.getDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration must be greater than 0");
        }
        if (exercise.getDate() == null || exercise.getDate().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is required");
        }
    }
}
