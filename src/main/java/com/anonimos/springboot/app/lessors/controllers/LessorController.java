package com.anonimos.springboot.app.lessors.controllers;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.services.LessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Optional;

@RestController
public class LessorController {
    @Autowired
    LessorService service;

    @GetMapping("/")
    public List<Lessor> findAll(){
        return service.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
       Optional<Lessor> lessorOptional = service.findLessorById(id);
       if(lessorOptional.isPresent()){
           return ResponseEntity.ok(lessorOptional.get());
       }
       return ResponseEntity.notFound()
    }

}
