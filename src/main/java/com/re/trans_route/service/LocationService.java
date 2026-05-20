package com.re.trans_route.service;

import com.re.trans_route.entity.Location;
import com.re.trans_route.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAllByOrderByNameAsc();
    }
}
