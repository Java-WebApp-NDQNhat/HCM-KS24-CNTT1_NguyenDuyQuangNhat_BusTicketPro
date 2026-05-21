package com.re.trans_route.controller.user_controller;

import com.re.trans_route.dto.TripDTO;
import com.re.trans_route.entity.Route;
import com.re.trans_route.entity.Seat;
import com.re.trans_route.entity.Trip;
import com.re.trans_route.entity.Ticket;
import com.re.trans_route.service.*;
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
    private final EmailService emailService;

    public PassengerController(RouteService routeService,
                               TripService tripService,
                               LocationService locationService,
                               SeatService seatService,
                               TicketService ticketService, EmailService emailService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.locationService = locationService;
        this.seatService = seatService;
        this.ticketService = ticketService;
        this.emailService = emailService;
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
    public String booking(Model model,
                          @RequestParam(required = false) String ticketCode,
                          @RequestParam(required = false) String phoneNum) {

        // Chưa nhập gì -> hiển thị trang trống
        if (!hasText(ticketCode) && !hasText(phoneNum)) {
            return "page/passenger/my-booking";
        }

        // Thiếu 1 trong 2 trường
        if (!hasText(ticketCode) || !hasText(phoneNum)) {
            model.addAttribute("inputError", "Vui lòng nhập đầy đủ mã vé và số điện thoại!");
            return "page/passenger/my-booking";
        }

        Ticket ticketFound = ticketService.findByTicketCodeAndPassengerPhone(ticketCode.trim(), phoneNum.trim());
        if (ticketFound == null) {
            model.addAttribute("noResult", true);
            return "page/passenger/my-booking";
        }

        model.addAttribute("ticket", ticketFound);
        model.addAttribute("canCancel", ticketService.canCancel(ticketFound));
        model.addAttribute("ticketCode", ticketCode.trim());
        model.addAttribute("phoneNum", phoneNum.trim());
        return "page/passenger/my-booking";
    }

    @PostMapping("/my-booking/cancel")
    public String cancelBooking(@RequestParam String ticketCode,
                                @RequestParam String phoneNum,
                                Model model) {
        if (!hasText(ticketCode) || !hasText(phoneNum)) {
            model.addAttribute("inputError", "Vui lòng nhập đầy đủ mã vé và số điện thoại!");
            return "page/passenger/my-booking";
        }

        try {
            Ticket cancelled = ticketService.cancelTicket(ticketCode.trim(), phoneNum.trim());
            model.addAttribute("ticket", cancelled);
            model.addAttribute("canCancel", false);
            model.addAttribute("ticketCode", ticketCode.trim());
            model.addAttribute("phoneNum", phoneNum.trim());
            model.addAttribute("cancelSuccess", "Hủy vé thành công!!!");
            return "page/passenger/my-booking";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Ticket ticket = ticketService.findByTicketCodeAndPassengerPhone(ticketCode.trim(), phoneNum.trim());
            if (ticket != null) {
                model.addAttribute("ticket", ticket);
                model.addAttribute("canCancel", ticketService.canCancel(ticket));
            }
            model.addAttribute("ticketCode", ticketCode.trim());
            model.addAttribute("phoneNum", phoneNum.trim());
            model.addAttribute("cancelError", ex.getMessage());
            return "page/passenger/my-booking";
        }
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

        emailService.sendEmail(
                passengerEmail,
                "Thông tin vé xe của bạn - Mã vé: " + ticketCode,
                "Xin chào " + passengerName + ",\n\n" +
                        "Cảm ơn bạn đã đặt vé tại Bus Ticket! Dưới đây là thông tin chi tiết về vé của bạn:\n\n" +
                        "Mã vé: " + ticketCode + "\n" +
                        "Hành trình: " + trip.getRoute().getFromLocation().getName() + " → " + trip.getRoute().getToLocation().getName() + "\n" +
                        "Ngày đi: " + trip.getDepartureTime().toLocalDate() + "\n" +
                        "Giờ đi: " + trip.getDepartureTime().toLocalTime() + "\n" +
                        "Số ghế: " + selectedSeat.getSeatNumber() + "\n" +
                        "Giá vé: $" + trip.getPrice() + "\n\n" +
                        "Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email này.\n\n" +
                        "Chúc bạn có một chuyến đi vui vẻ!\n" +
                        "- Đội ngũ TransRoute"
        );

        return "page/passenger/ticket-booking/payment-success";
    }


}
