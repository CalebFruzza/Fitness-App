package com.exercise.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.backend.model.Supplement;
import com.exercise.backend.repository.SupplementRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/supplements")
@CrossOrigin(origins = "*")
@Validated
public class SupplementController {
    private final SupplementRepository repository;

    public SupplementController(SupplementRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Supplement> addSupplement(@Valid @RequestBody Supplement supplement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(supplement));
    }

    @GetMapping({"/user/{userId}", "/user/{userId}/history"})
    public List<Supplement> getUserSupplements(@PathVariable @Positive(message = "User ID must be greater than 0") Long userId) {
        return repository.findByUserIdOrderByDateTakenDescIdDesc(userId);
    }
}
