package com.re.trans_route.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.re.trans_route.entity.Bus;
import com.re.trans_route.entity.Seat;
import com.re.trans_route.entity.Trip;
import com.re.trans_route.repository.TripRepository;
import com.re.trans_route.type.SeatStatus;

@Service
public class TripService {
    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Page<Trip> getAllTrips(Pageable pageable) {
        return tripRepository.findAll(pageable);
    }

    @Transactional
    public void saveTrip(Trip trip) {
        // set default cho seat la` available
        Bus bus = trip.getBus();
        if (bus != null && bus.getTotalSeats() != null) {
            List<Seat> seats = new ArrayList<>();
            for (int i = 1; i <= bus.getTotalSeats(); i++) {
                Seat seat = Seat.builder()
                        .trip(trip)
                        .seatNumber(i)
                        .status(SeatStatus.AVAILABLE)
                        .build();
                seats.add(seat);
            }
            trip.setSeats(seats);
        }
        tripRepository.save(trip);
    }

    public Trip getTripById(Long tripId) {
        return tripRepository.findById(tripId).orElse(null);
    }

    public List<Trip> searchByDate(Long routeId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return tripRepository.findByRouteAndDateRange(routeId, startOfDay, endOfDay);
    }

    public List<Trip> searchTrips(String fromPlace, String toPlace, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return tripRepository.searchByPlacesAndDate(
                fromPlace.trim(),
                toPlace.trim(),
                startOfDay,
                endOfDay);
    }

}
