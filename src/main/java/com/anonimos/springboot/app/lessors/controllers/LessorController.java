package com.anonimos.springboot.app.lessors.controllers;

import com.anonimos.springboot.app.lessors.models.Car;
import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.services.LessorService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@Tag(name = "Lessors", description = "Microservice LESSORS")
public class LessorController {
    @Autowired
    LessorService service;

    @Operation( summary = "List Lessors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lessors Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Lessors not Found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<Lessor>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation( summary = "Get a Lessor Detail by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lessor Detail Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Lessor Detail not Found", content = @Content)
    })
    @GetMapping("/{id}") //getByID -> Details
    public ResponseEntity<?> detail(@PathVariable Long id){
       Optional<Lessor> o = service.findByIdWithCars(id);     //service.findLessorById(id);
       if(o.isPresent()){
           return ResponseEntity.ok(o.get());
       }
       return ResponseEntity.notFound().build();
    }
    @Operation( summary = "Delete a Lessor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lessor deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Lessor not Found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable Long id) {
        Optional<Lessor> l = service.findLessorById(id);
        if(l.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }

    @Operation( summary = "Add a new Lessor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lessor added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
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

    @Operation( summary = "Update a Lessor by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lessor Updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Lessor not found", content = @Content)
    })
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

    @Operation( summary = "assign a car to a Lessor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car assigned", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)
    })
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

    @Operation( summary = "Create a car by a Lessor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)
    })
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

    @Operation( summary = "unAssign a car to a Lessor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car unassigned", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)
    })
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

    @Operation( summary = "Delete a lessorCar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)
    })
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
