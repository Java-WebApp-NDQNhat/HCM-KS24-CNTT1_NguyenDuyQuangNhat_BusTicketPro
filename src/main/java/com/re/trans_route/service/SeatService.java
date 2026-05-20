package com.re.trans_route.service;

import com.re.trans_route.entity.Seat;
import com.re.trans_route.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getByTripId(Long id) {
        return seatRepository.findByTripId(id);
    }

    public Seat getById(Long id) {
        return seatRepository.findById(id).orElse(null);
    }

    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }
}
