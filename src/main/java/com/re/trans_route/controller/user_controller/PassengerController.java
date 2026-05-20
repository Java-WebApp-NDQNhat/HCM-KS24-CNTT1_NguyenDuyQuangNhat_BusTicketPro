package com.re.trans_route.controller.user_controller;

import com.re.trans_route.dto.TripDTO;
import com.re.trans_route.entity.Route;
import com.re.trans_route.entity.Seat;
import com.re.trans_route.entity.Trip;
import com.re.trans_route.entity.Ticket;
import com.re.trans_route.service.LocationService;
import com.re.trans_route.service.RouteService;
import com.re.trans_route.service.SeatService;
import com.re.trans_route.service.TicketService;
import com.re.trans_route.service.TripService;
import com.re.trans_route.type.BookingStatus;
import com.re.trans_route.type.SeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/passenger")
public class PassengerController {
    private final RouteService routeService;
    private final TripService tripService;
    private final LocationService locationService;
    private final SeatService seatService;
    private final TicketService ticketService;

    public PassengerController(RouteService routeService,
                               TripService tripService,
                               LocationService locationService,
                               SeatService seatService,
                               TicketService ticketService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.locationService = locationService;
        this.seatService = seatService;
        this.ticketService = ticketService;
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

        if (travelDate.isBefore(LocalDate.now())) {
            result.rejectValue("travelDate", "pastDate", "Ngày đi phải là ngày hiện tại hoặc tương lai");
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

    @GetMapping("/my-booking")
    public String booking(Model model) {
        return "page/passenger/my-booking";
    }

    @GetMapping("/booking/{id}")
    public String bookTrip(Model model,
                           @PathVariable("id") Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        List<Seat> seats = seatService.getByTripId(tripId);

        model.addAttribute("seats", seats);
        model.addAttribute("trip_info", trip);
        model.addAttribute("fromLocation", trip.getRoute().getFromLocation().getName());
        model.addAttribute("toLocation", trip.getRoute().getToLocation().getName());

        return "page/passenger/ticket-booking/seat-selection";
    }

    @PostMapping("/booking/payment-checkout")
    @Transactional
    public String paymentCheckout(Model model,
                                  @RequestParam("tripId") Long tripId,
                                  @RequestParam("seatId") Long seatId) {
        Trip trip = tripService.getTripById(tripId);
        Seat selectedSeat = seatService.getById(seatId);

        // chuyen? trang. thai' sang pending trong 10ph
        selectedSeat.setStatus(SeatStatus.PENDING);
        seatService.save(selectedSeat);


        model.addAttribute("trip_info", trip);
        model.addAttribute("seat", selectedSeat);
        model.addAttribute("fromLocation", trip.getRoute().getFromLocation().getName());
        model.addAttribute("toLocation", trip.getRoute().getToLocation().getName());

        return "page/passenger/ticket-booking/payment-checkout";
    }

    @PostMapping("/booking/payment-success")
    @Transactional
    public String paymentSuccess(Model model,
                                 @RequestParam("tripId") Long tripId,
                                 @RequestParam("seatId") Long seatId,
                                 @RequestParam("passengerName") String passengerName,
                                 @RequestParam("passengerEmail") String passengerEmail,
                                 @RequestParam("passengerPhone") String passengerPhone) {
        Trip trip = tripService.getTripById(tripId);
        Seat selectedSeat = seatService.getById(seatId);

        // Cập nhật trạng thái ghế thành BOOKED
        selectedSeat.setStatus(SeatStatus.BOOKED);
        seatService.save(selectedSeat);

        // Tạo mã vé duy nhất
        String ticketCode = "TR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Ticket ticket = Ticket.builder()
                .passengerName(passengerName)
                .passengerEmail(passengerEmail)
                .passengerPhone(passengerPhone)
                .trip(trip)
                .seat(selectedSeat)
                .price(trip.getPrice())
                .status(BookingStatus.PAID)
                .ticketCode(ticketCode)
                .build();

        Ticket savedTicket = ticketService.save(ticket);

        model.addAttribute("ticket", savedTicket);
        model.addAttribute("trip_info", trip);
        model.addAttribute("fromLocation", trip.getRoute().getFromLocation().getName());
        model.addAttribute("toLocation", trip.getRoute().getToLocation().getName());

        return "page/passenger/ticket-booking/payment-success";
    }
}
