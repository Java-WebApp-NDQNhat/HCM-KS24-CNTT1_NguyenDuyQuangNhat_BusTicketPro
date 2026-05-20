package com.re.trans_route.controller.user_controller;

import com.re.trans_route.dto.TripDTO;
import com.re.trans_route.entity.Route;
import com.re.trans_route.entity.Trip;
import com.re.trans_route.service.LocationService;
import com.re.trans_route.service.RouteService;
import com.re.trans_route.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/passenger")
public class PassengerController {
    private final RouteService routeService;
    private final TripService tripService;
    private final LocationService locationService;

    public PassengerController(RouteService routeService,
                               TripService tripService,
                               LocationService locationService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.locationService = locationService;
    }

    @ModelAttribute("search")
    public TripDTO searchForm() {
        return new TripDTO();
    }

    @GetMapping("/dashboard")
    public String passengerDashboard(Model model,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam(required = false) String fromPlace,
                                     @RequestParam(required = false) String toPlace,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate,
                                     @ModelAttribute("search") TripDTO search,
                                     BindingResult result) {
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("locations", locationService.getAllLocations());

        search.setFromPlace(fromPlace);
        search.setToPlace(toPlace);
        search.setTravelDate(travelDate);
        model.addAttribute("search", search);

        boolean isSearch = hasText(fromPlace) && hasText(toPlace) && travelDate != null;
        if (!isSearch) {
            return "page/passenger/dashboard";
        }

        validateSearch(fromPlace, toPlace, travelDate, result);
        if (result.hasErrors()) {
            return "page/passenger/dashboard";
        }

        if (fromPlace.trim().equalsIgnoreCase(toPlace.trim())) {
            result.rejectValue("toPlace", "samePlace", "Điểm đến phải khác điểm đi");
            return "page/passenger/dashboard";
        }

        Long routeId = routeService.getRouteIdByPlaces(fromPlace.trim(), toPlace.trim());
        if (routeId == null) {
            model.addAttribute("routeNotFound", true);
            model.addAttribute("tripPage", Collections.emptyList());
            model.addAttribute("totalItems", 0L);
            return "page/passenger/dashboard";
        }

        List<Trip> trips = tripService.searchTrips(fromPlace, toPlace, travelDate);
        model.addAttribute("tripPage", trips);
        model.addAttribute("totalItems", (long) trips.size());
        return "page/passenger/dashboard";
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void validateSearch(String fromPlace, String toPlace, LocalDate travelDate, BindingResult result) {
        if (!hasText(fromPlace)) {
            result.rejectValue("fromPlace", "required", "Điểm đi không được để trống");
        }
        if (!hasText(toPlace)) {
            result.rejectValue("toPlace", "required", "Điểm đến không được để trống");
        }
        if (travelDate == null) {
            result.rejectValue("travelDate", "required", "Ngày đi không được để trống");
        }
    }

    @GetMapping("/routes")
    public String routePlanning(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Route> routePage = routeService.getAllRoutes(pageable);

        model.addAttribute("routePage", routePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", routePage.getTotalPages());
        model.addAttribute("totalItems", routePage.getTotalElements());

        return "/page/passenger/route-planning";
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        return "page/passenger/my-booking";
    }
}
