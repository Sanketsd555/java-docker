package com.thinkconstructive.docker_demo.service;

import com.thinkconstructive.docker_demo.model.Car;
import com.thinkconstructive.docker_demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Car addCar(Car car) {
        car.setAvailable(true);
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car updatedCar) {
        return carRepository.findById(id).map(car -> {
            car.setMake(updatedCar.getMake());
            car.setModel(updatedCar.getModel());
            car.setYear(updatedCar.getYear());
            car.setColor(updatedCar.getColor());
            car.setPricePerDay(updatedCar.getPricePerDay());
            car.setAvailable(updatedCar.isAvailable());
            car.setLicensePlate(updatedCar.getLicensePlate());
            return carRepository.save(car);
        }).orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> getCarsByMake(String make) {
        return carRepository.findByMakeIgnoreCase(make);
    }

    public List<Car> getCarsByMaxPrice(double maxPrice) {
        return carRepository.findByPricePerDayLessThanEqual(maxPrice);
    }
}
