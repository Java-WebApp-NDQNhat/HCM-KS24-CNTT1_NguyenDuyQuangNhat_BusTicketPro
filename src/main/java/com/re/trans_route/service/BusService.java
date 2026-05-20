package com.re.trans_route.service;

import com.re.trans_route.entity.Bus;
import com.re.trans_route.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BusService {
    private final BusRepository busRepository;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public Page<Bus> getAllBuses(Pageable pageable) {
        return busRepository.findAll(pageable);
    }

    public void saveBus(Bus bus) {
        busRepository.save(bus);
    }

    public Bus getBusById(Long busId) {
        return busRepository.findById(busId).orElse(null);
    }

    public void deleteBus(Long busId) {
        busRepository.deleteById(busId);
    }
}
