package com.anonimos.springboot.app.lessors.controllers;

import com.anonimos.springboot.app.lessors.models.Car;
import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.services.LessorService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class LessorController {
    @Autowired
    LessorService service;


    @GetMapping("/")
    public ResponseEntity<List<Lessor>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }


    @GetMapping("/{id}") //getByID -> Details
    public ResponseEntity<?> detail(@PathVariable Long id){
       Optional<Lessor> o = service.findByIdWithCars(id);     //service.findLessorById(id);
       if(o.isPresent()){
           return ResponseEntity.ok(o.get());
       }
       return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable Long id) {
        Optional<Lessor> l = service.findLessorById(id);
        if(l.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }


    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody Lessor lessor,BindingResult result){
        if(result.hasErrors()){
            return validate(result);
        }
        if(!lessor.getEmail().isEmpty() &&  service.existByEmail(lessor.getEmail())){
            return ResponseEntity.badRequest().
                    body(Collections.
                            singletonMap("error", "There is already a user with that email address!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(lessor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Lessor lessor, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()){
            return validate(result);
        }
        Optional<Lessor> l = service.findLessorById(id);
        if (l.isPresent()){
            Lessor lessorDb = l.get();
            if(!lessor.getEmail().isEmpty()
                    && !lessor.getEmail().equalsIgnoreCase(lessorDb.getEmail())
                    && service.findByEmail(lessor.getEmail()).isPresent()){
                return ResponseEntity.badRequest().
                        body(Collections.
                                singletonMap("error", "There is already a user with that email address!"));
            }
            return new ResponseEntity<>(service.update(id,lessor),HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    /**Microservices Iteration*/

    @PutMapping( "/assign-car/{lessorId}")
    public ResponseEntity<?> assignCar(@RequestBody Car car,  @PathVariable Long lessorId){
       Optional<Car> o ;
       try {
          o = service.assignCar(car,lessorId);
       }catch (FeignException exception ){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message","Does not exist car by id or communication error: " +
                   exception.getMessage()));
       }

       if(o.isPresent()){
           return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
       }
       return ResponseEntity.notFound().build();

    }

    @PostMapping("/create-car/{lessorId}")
    public ResponseEntity<?> createCar(@RequestBody Car car,  @PathVariable Long lessorId){
        Optional<Car> o ;
        try {
            o = service.createCar(car,lessorId);
        }catch (FeignException exception ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message","Can't create the car or communication error : " +
                    exception.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/unAssign-car/{lessorId}")
    public ResponseEntity<?> deleteCar(@RequestBody Car car,  @PathVariable Long lessorId){
        Optional<Car> o ;
        try {
            o = service.unAssignCar(car,lessorId);
        }catch (FeignException exception ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message","Can't create the car or communication error : " +
                    exception.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete_car/{id}")
    public ResponseEntity<?> deleteLessorCar(@PathVariable Long id){
        service.deleteLessorCarById(id);
        return ResponseEntity.noContent().build();
    }


    /**validation*/
    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
