package com.anonimos.springboot.app.lessors.services.serviceImpl;

import com.anonimos.springboot.app.lessors.clients.CarClientRest;
import com.anonimos.springboot.app.lessors.models.Car;
import com.anonimos.springboot.app.lessors.models.entity.Lessor;
import com.anonimos.springboot.app.lessors.models.entity.LessorCar;
import com.anonimos.springboot.app.lessors.repositories.LessorRepository;
import com.anonimos.springboot.app.lessors.services.LessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LessorServiceImpl implements LessorService {
    @Autowired
    private LessorRepository lessorRepository;

    @Autowired
    private CarClientRest clientRest;

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
    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return lessorRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Lessor> findByEmail(String email) {
        return lessorRepository.getByEmail(email);
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
                            l.setUsername(newL.getUsername());
                            l.setPassword(newL.getPassword());
                            return lessorRepository.save(l);
                        }
                ).get();
    }

/**Microservices-iterations*/
    @Override
    @Transactional(readOnly = true)
    public Optional<Lessor> findByIdWithCars(Long id) {
        Optional<Lessor> o = lessorRepository.findById(id);
        if(o.isPresent()){
            Lessor lessor = o.get();
            if(!lessor.getLessorCars().isEmpty()){
                List<Long> ids = lessor.getLessorCars().stream().map(
                        lessorCar -> lessorCar.getCarId()).collect(Collectors.toList());
                List<Car> cars = clientRest.getCarsByLessor(ids);
                lessor.setCars(cars);
            }
            return Optional.of(lessor);
        }
        return Optional.empty();
    }
    @Override
    @Transactional
    public Optional<Car> assignCar(Car car, Long lessorId) {
        Optional<Lessor> o = lessorRepository.findById(lessorId);
        if(o.isPresent()){
            Car carMsvc = clientRest.getById(car.getIdCar());
            Lessor lessor= o.get();
            LessorCar lessorCar = new LessorCar();
            lessorCar.setCarId(carMsvc.getIdCar());

            lessor.addLessorCar(lessorCar);
            lessorRepository.save(lessor);

            return Optional.of(carMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Car> createCar(Car car, Long lessorId) {
        Optional<Lessor> o = lessorRepository.findById(lessorId);
        if(o.isPresent()){
            Car newCarMsvc = clientRest.create(car);

            Lessor lessor= o.get();
            LessorCar lessorCar = new LessorCar();
            lessorCar.setCarId(newCarMsvc.getIdCar());

            lessor.addLessorCar(lessorCar);
            lessorRepository.save(lessor);
            return Optional.of(newCarMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Car> unAssignCar(Car car, Long lessorId) {
        Optional<Lessor> o = lessorRepository.findById(lessorId);
        if(o.isPresent()){
            Car carMsvc = clientRest.getById(car.getIdCar());

            Lessor lessor= o.get();
            LessorCar lessorCar = new LessorCar();
            lessorCar.setCarId(carMsvc.getIdCar());

            lessor.removeLessorCar(lessorCar);
            lessorRepository.save(lessor);

            return Optional.of(carMsvc);
        }
        return Optional.empty();
    }
}
