package com.thinkconstructive.docker_demo.repository;

import com.thinkconstructive.docker_demo.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByAvailableTrue();

    List<Car> findByMakeIgnoreCase(String make);

    List<Car> findByPricePerDayLessThanEqual(double maxPrice);
}
