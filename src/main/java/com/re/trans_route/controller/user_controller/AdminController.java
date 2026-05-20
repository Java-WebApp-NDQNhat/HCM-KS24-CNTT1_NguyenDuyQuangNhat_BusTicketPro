package com.re.trans_route.controller.user_controller;

import com.re.trans_route.entity.Bus;
import com.re.trans_route.entity.Route;
import com.re.trans_route.entity.Trip;
import com.re.trans_route.service.BusService;
import com.re.trans_route.service.RouteService;
import com.re.trans_route.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RouteService routeService;
    private final BusService busService;
    private final TripService tripService;

    @Autowired
    public AdminController(RouteService routeService, BusService busService, TripService tripService) {
        this.routeService = routeService;
        this.busService = busService;
        this.tripService = tripService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "page/admin/dashboard";
    }

    @GetMapping("/fleet")
    public String fleetManagement(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bus> busPage = busService.getAllBuses(pageable);

        model.addAttribute("busPage", busPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", busPage.getTotalPages());
        model.addAttribute("totalItems", busPage.getTotalElements());

        return "fragments/admin/bus-management";
    }

    @GetMapping("/fleet/add")
    public String addBusForm(Model model) {
        Bus bus = new Bus();
        bus.setStatus("Active");
        model.addAttribute("bus", bus);
        model.addAttribute("allRoutes", routeService.getAllRoutesNoPagination());

        model.addAttribute("plateNumErr", null);
        return "fragments/admin/bus-add-form";
    }

    @PostMapping("/fleet/save")
    public String addBus(@Valid @ModelAttribute("bus") Bus bus,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoutes", routeService.getAllRoutesNoPagination());
            return "fragments/admin/bus-add-form";
        }

        try {
            busService.saveBus(bus);
        } catch (DataIntegrityViolationException e) {
            // bat' unique cua? platenumber
            model.addAttribute("plateNumErr", "Biển số xe (Plate Number) đã tồn tại trong hệ thống!");
            model.addAttribute("allRoutes", routeService.getAllRoutesNoPagination());
            return "fragments/admin/bus-add-form";
        }
        return "redirect:/admin/fleet";
    }

    @GetMapping("/fleet/edit/{id}")
    public String editBusForm(Model model, @PathVariable("id") Long busId) {
        Bus bus = busService.getBusById(busId);
        if (bus == null) {
            return "redirect:/admin/fleet";
        }
        model.addAttribute("plateNumErr", null);

        model.addAttribute("bus", bus);
        model.addAttribute("allRoutes", routeService.getAllRoutesNoPagination());
        return "fragments/admin/bus-add-form";
    }

    @GetMapping("/fleet/delete/{id}")
    public String deleteBus(@PathVariable("id") Long busId) {
        busService.deleteBus(busId);
        return "redirect:/admin/fleet";
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

        return "fragments/admin/route-planning";
    }

    @GetMapping("/staff")
    public String staffDirectory() {
        return "fragments/admin/staff-directory";
    }

    @GetMapping("/trips")
    public String tripManagement(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Trip> tripPage = tripService.getAllTrips(pageable);

        model.addAttribute("tripPage", tripPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", tripPage.getTotalPages());
        model.addAttribute("totalItems", tripPage.getTotalElements());

        return "fragments/admin/trip-management";
    }

    @GetMapping("/trips/add")
    public String addTripForm(Model model) {
        Trip trip = new Trip();
        model.addAttribute("trip", trip);
        model.addAttribute("allRoutes", routeService.getAllRoutesNoPagination());
        model.addAttribute("allBuses", busService.getAllBusesNoPagination());
        return "fragments/admin/trip-add-form";
    }

    @PostMapping("/trips/save")
    public String addTrip(@Valid @ModelAttribute("trip") Trip trip,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors() || trip.getRoute() == null || trip.getRoute().getId() == null || trip.getBus() == null || trip.getBus().getId() == null) {
            model.addAttribute("allRoutes", routeService.getAllRoutesNoPagination());
            model.addAttribute("allBuses", busService.getAllBusesNoPagination());
            return "fragments/admin/trip-add-form";
        }

        Route route = routeService.getRouteById(trip.getRoute().getId());
        Bus bus = busService.getBusById(trip.getBus().getId());
        trip.setRoute(route);
        trip.setBus(bus);

        tripService.saveTrip(trip);
        return "redirect:/admin/trips";
    }
}
