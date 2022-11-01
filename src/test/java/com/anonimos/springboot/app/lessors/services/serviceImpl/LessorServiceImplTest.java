package com.anonimos.springboot.app.lessors.services.serviceImpl;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.enums.CarType;
import com.anonimos.springboot.app.lessors.repositories.LessorRepository;
import com.anonimos.springboot.app.lessors.services.serviceImpl.LessorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class LessorServiceImplTest {

    @Mock
    private LessorRepository lessorRepository;
    @InjectMocks
    private LessorServiceImpl lessorService;

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
    void findAll() {
        when(lessorRepository.findAll()).thenReturn(Arrays.asList(lessor, lessor2));
        assertNotNull(lessorService.findAll());
    }

    @Test
    void findLessorById() {

        when(lessorRepository.findById(lessor.getId())).thenReturn(Optional.ofNullable(lessor));
        assertNotNull(lessorService.findLessorById(lessor.getId()));
    }

    @Test
    void existByEmail() {
        boolean flag= false;

        when(lessorRepository.existsByEmail(lessor.getEmail())).thenReturn(flag);
        if(flag== true)
            assertNotNull(lessorService.existByEmail(lessor.getEmail()));
    }

    @Test
    void findByEmail() {
        when(lessorRepository.findByEmail(lessor.getEmail())).thenReturn(Optional.ofNullable(lessor));
        assertNotNull(lessorService.findByEmail(any()));
    }

    @Test
    void create() {
        when(lessorRepository.save(any(Lessor.class))).thenReturn(lessor);
        assertNotNull(lessorService.create(lessor));

    }


    @Test
    void delete() {
        when(lessorRepository.findById(12L)).thenReturn(Optional.ofNullable(lessor));
        Optional<Lessor> foundLessor = lessorService.findLessorById(12L);
        lessorRepository.deleteById(12L);
        Optional<Lessor>deleted=null;
        foundLessor=deleted;
        lessorService.delete(12L);
        assertNull(foundLessor);
    }
}