package com.anonimos.springboot.app.lessors.repositories;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LessorRepository extends CrudRepository<Lessor,Long> {

    /**Just to know the different ways to do the same thing*/
    Optional<Lessor> findByEmail(String email);
    @Query("SELECT l FROM Lessor l WHERE l.email =?1 ")
    Optional<Lessor> getByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Query("DELETE FROM LessorCar lc WHERE lc.carId=?1")
    void deleteLessorCarByID(Long id);

}
