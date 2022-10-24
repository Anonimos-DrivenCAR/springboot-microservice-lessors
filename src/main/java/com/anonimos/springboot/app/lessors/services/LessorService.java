package com.anonimos.springboot.app.lessors.services;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;

import java.util.List;
import java.util.Optional;

public interface LessorService {
    List<Lessor> findAll();
    Optional<Lessor> findLessorById(Long id);
    Lessor save(Lessor lessor);
    void delete(Long id);
    void create(Lessor newLessor);
    Lessor update(Long id, Lessor lessor);
}
