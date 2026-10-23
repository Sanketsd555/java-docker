package com.thinkconstructive.docker_demo.service;

import com.thinkconstructive.docker_demo.model.Booking;
import com.thinkconstructive.docker_demo.model.Car;
import com.thinkconstructive.docker_demo.repository.BookingRepository;
import com.thinkconstructive.docker_demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    public Booking createBooking(Booking booking) {
        Car car = carRepository.findById(booking.getCar().getId())
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + booking.getCar().getId()));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available for booking.");
        }

        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (days <= 0) {
            throw new RuntimeException("End date must be after start date.");
        }

        double totalPrice = days * car.getPricePerDay();
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("CONFIRMED");

        // Mark car as unavailable
        car.setAvailable(false);
        carRepository.save(car);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByCustomerEmail(email);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is already cancelled.");
        }

        booking.setStatus("CANCELLED");

        // Release the car
        Car car = booking.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        return bookingRepository.save(booking);
    }

    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        booking.setStatus("COMPLETED");

        // Release the car
        Car car = booking.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        return bookingRepository.save(booking);
    }
}
