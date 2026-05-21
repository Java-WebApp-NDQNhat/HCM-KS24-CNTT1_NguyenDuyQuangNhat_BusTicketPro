package com.re.trans_route.service;

import com.re.trans_route.entity.Ticket;
import com.re.trans_route.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket findByTicketCodeAndPassengerPhone(String ticketCode, String passengerPhone) {
        return ticketRepository.findByTicketCodeAndPassengerPhone(ticketCode, passengerPhone);
    }
}
