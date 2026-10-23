package com.thinkconstructive.docker_demo.controller;

import com.thinkconstructive.docker_demo.model.Car;
import com.thinkconstructive.docker_demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*")
public class CarController {

    @Autowired
    private CarService carService;

    // GET all cars
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    // GET available cars only
    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    // GET car by ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET cars by make
    @GetMapping("/make/{make}")
    public ResponseEntity<List<Car>> getCarsByMake(@PathVariable String make) {
        return ResponseEntity.ok(carService.getCarsByMake(make));
    }

    // GET cars by max price per day
    @GetMapping("/price/{maxPrice}")
    public ResponseEntity<List<Car>> getCarsByMaxPrice(@PathVariable double maxPrice) {
        return ResponseEntity.ok(carService.getCarsByMaxPrice(maxPrice));
    }

    // POST add a new car
    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.addCar(car));
    }

    // PUT update a car
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        try {
            return ResponseEntity.ok(carService.updateCar(id, car));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE a car
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok("Car deleted successfully.");
    }
}
