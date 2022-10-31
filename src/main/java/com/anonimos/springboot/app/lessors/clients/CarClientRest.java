package com.anonimos.springboot.app.lessors.clients;

import com.anonimos.springboot.app.lessors.models.Car;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-cars", url = "localhost:8001")

public interface CarClientRest {

    @GetMapping(value="/id/{id}")
    Car getById(@PathVariable Long id);
    @PostMapping(value = "/")
    Car create(@RequestBody Car car);
    @GetMapping(value="/cars-by-lessor")
    List<Car>  getCarsByLessor(@RequestParam Iterable<Long> ids);
}
