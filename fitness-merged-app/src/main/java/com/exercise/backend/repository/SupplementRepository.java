package com.exercise.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.backend.model.Supplement;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {
    List<Supplement> findByUserIdOrderByDateTakenDescIdDesc(Long userId);
}
