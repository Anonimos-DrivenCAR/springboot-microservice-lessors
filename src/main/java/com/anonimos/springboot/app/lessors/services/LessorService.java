package com.anonimos.springboot.app.lessors.services;

import com.anonimos.springboot.app.lessors.models.Car;
import com.anonimos.springboot.app.lessors.models.entity.Lessor;

import java.util.List;
import java.util.Optional;

public interface LessorService {
    List<Lessor> findAll();
    Optional<Lessor> findLessorById(Long id);
    Optional<Lessor> findByEmail(String email);
    void delete(Long id);
    Lessor create(Lessor newLessor);
    Lessor update(Long id, Lessor lessor);
    boolean existByEmail(String email);




    /**Microservices-iteration*/
    Optional<Lessor> findByIdWithCars(Long id);
    Optional<Car> assignCar(Car car,Long lessorId);
    Optional<Car> createCar(Car car,Long LessorId);
    Optional<Car> unAssignCar(Car car,Long lessorId);



}
