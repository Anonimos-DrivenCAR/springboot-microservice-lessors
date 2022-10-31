package com.anonimos.springboot.app.lessors.repositories;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface LessorRepository extends CrudRepository<Lessor,Long> {
    Optional<Lessor> findByEmail(String email);
    @Query("select l from Lessor l where l.email =?1 ")
    Optional<Lessor> getByEmail(String email);
    boolean existsByEmail(String email);

}
