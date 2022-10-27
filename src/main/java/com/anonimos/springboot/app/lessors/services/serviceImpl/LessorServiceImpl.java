package com.anonimos.springboot.app.lessors.services.serviceImpl;

import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.repositories.LessorRepository;
import com.anonimos.springboot.app.lessors.services.LessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

@Service
public class LessorServiceImpl implements LessorService {
    @Autowired
    LessorRepository lessorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Lessor> findAll() {
        return (List<Lessor>) lessorRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Lessor> findLessorById(Long id) {
        return lessorRepository.findById(id);
    }

    @Override
    @Transactional
    public Lessor create(Lessor newLessor) {
        return lessorRepository.save(newLessor);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        lessorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Lessor update(Long id, Lessor newL) {
         return lessorRepository.findById(id)
                .map(
                        l->{
                            l.setName(newL.getName());
                            l.setEmail(newL.getEmail());
                            l.setPassword(newL.getPassword());
                            return lessorRepository.save(l);
                        }
                ).get();
    }
}
