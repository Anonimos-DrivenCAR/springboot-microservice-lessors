package com.anonimos.springboot.app.lessors.repositories;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface LessorRepository extends CrudRepository<Lessor,Long> {
}
