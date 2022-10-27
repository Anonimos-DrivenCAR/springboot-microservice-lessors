package com.anonimos.springboot.app.lessors.controllers;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.services.LessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
       return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody Lessor lessor,BindingResult result){
        if(result.hasErrors()){
            return validate(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(lessor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody Lessor lessor, BindingResult result){

        if(result.hasErrors()){
            return validate(result);
        }

        Optional<Lessor> l = service.findLessorById(id);
        if (l.isPresent()){
            return new ResponseEntity<>(service.update(id,lessor),HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@Valid @PathVariable Long id) {
        Optional<Lessor> l = service.findLessorById(id);
        if(l.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(), "Field " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
