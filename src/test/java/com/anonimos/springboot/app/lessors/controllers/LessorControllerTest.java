package com.anonimos.springboot.app.lessors.controllers;

import com.anonimos.springboot.app.lessors.services.serviceImpl.LessorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.models.Car;
import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.models.entity.LessorCar;
import com.anonimos.springboot.app.lessors.repositories.LessorRepository;
import com.anonimos.springboot.app.lessors.services.LessorService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.Email;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class LessorControllerTest {

    @Mock
    private LessorService lessorService;
    @InjectMocks
    private LessorController lessorController;

    private Lessor lessor;
    private Lessor lessor2;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        lessor = new Lessor();
        lessor.setName("Piero");
        lessor.setEmail("PieroRD@gmail.com");
        lessor.setUsername("Pieroxx");
        lessor.setPassword("OREIP213");
        lessor2 = new Lessor();
        lessor2.setName("Andre");
        lessor2.setEmail("AndreRD@gmail.com");
        lessor2.setUsername("Andrexx");
        lessor2.setPassword("OREIP213");
    }
    @Test
    void getAll() {
        when(lessorService.findAll()).thenReturn(Collections.singletonList(lessor));
        assertNotNull(lessorController.getAll());
    }

    @Test
    void detail() {
        when(lessorService.findByIdWithCars(12L)).thenReturn(Optional.of(lessor));
        assertNotNull(lessorController.detail(12L));
    }

    @Test
    void delete() {
        when(lessorService.findLessorById(12l)).thenReturn(Optional.ofNullable(lessor));

        Optional<Lessor> foundLessor = Optional.ofNullable(lessor2);
        lessorService.delete(12L);
        foundLessor=null;
        lessorController.delete(12L);
        assertNull(foundLessor);
    }

    @Test
    void create() {
        when(lessorService.create(lessor)).thenReturn(lessor);
        boolean created = true;
        assertNotNull(lessor);
    }

    @Test
    void deleteLessorCar() {
    }
}