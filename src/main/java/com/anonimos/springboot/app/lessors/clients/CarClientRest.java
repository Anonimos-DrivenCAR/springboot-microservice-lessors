package com.anonimos.springboot.app.lessors.clients;

import com.anonimos.springboot.app.lessors.models.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-cars", url = "${msvc.cars.url}")
public interface CarClientRest {

    @GetMapping("/{id}")
    Car getById(@PathVariable Long id);
    @PostMapping(value = "/")
    Car create(@RequestBody Car car);
    @GetMapping(value="/cars-by-lessor")
    List<Car>  getCarsByLessor(@RequestParam Iterable<Long> ids);
}
