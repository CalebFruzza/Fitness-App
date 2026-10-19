package com.exercise.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.exercise.backend.model.Exercise;
import com.exercise.backend.repository.ExerciseRepository;

@Service
public class ExerciseService {
    private static final Map<String, Double> MET_VALUES = Map.of(
        "running", 8.3,
        "cycling", 6.8,
        "swimming", 7.0,
        "walking", 3.8,
        "jumping-jacks", 10.0
    );

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
        Exercise exercise = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        validate(updated);
        exercise.setName(updated.getName());
        exercise.setDuration(updated.getDuration());
        exercise.setDate(updated.getDate());
        exercise.setCalories(calculateCalories(updated.getName(), updated.getDuration()));
        return repository.save(exercise);
    }

    public int getCaloriesBetween(String startDate, String endDate) {
        return repository.findByDateBetween(startDate, endDate).stream().mapToInt(Exercise::getCalories).sum();
    }

    private int calculateCalories(String name, int duration) {
        double met = MET_VALUES.get(name);
        return (int) Math.round(met * REFERENCE_WEIGHT_KG * duration / 60.0);
    }

    private void validate(Exercise exercise) {
        if (exercise.getName() == null || !MET_VALUES.containsKey(exercise.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid exercise name. Must be one of: " + MET_VALUES.keySet());
        }
        if (exercise.getDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration must be greater than 0");
        }
        if (exercise.getDate() == null || exercise.getDate().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is required");
        }
    }
}
