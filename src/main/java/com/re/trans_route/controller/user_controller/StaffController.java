package com.re.trans_route.controller.user_controller;

import com.re.trans_route.entity.Ticket;
import com.re.trans_route.service.TicketService;
import com.re.trans_route.type.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff")
public class StaffController {
    private final TicketService ticketService;

    public StaffController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> ticketPage = ticketService.getAllTickets(pageable);

        model.addAttribute("tickets", ticketPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", ticketPage.getTotalPages());
        model.addAttribute("totalItems", ticketPage.getTotalElements());

        return "/page/staff/dashboard";
    }

    @PostMapping("/ticket/confirm/{id}")
    public String confirmTicket(@PathVariable("id") Long ticketId, Model model) {
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket != null) {
            ticket.setStatus(BookingStatus.CONFIRM);
            ticketService.save(ticket);
            return "redirect:/staff/dashboard";
        }

        model.addAttribute("error", "Ticket not found");
        return "/page/staff/dashboard";
    }
}
