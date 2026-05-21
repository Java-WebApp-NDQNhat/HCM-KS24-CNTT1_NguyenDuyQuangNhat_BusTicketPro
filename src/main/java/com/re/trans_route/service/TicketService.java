package com.re.trans_route.service;

import com.re.trans_route.entity.Seat;
import com.re.trans_route.entity.Ticket;
import com.re.trans_route.repository.SeatRepository;
import com.re.trans_route.repository.TicketRepository;
import com.re.trans_route.type.BookingStatus;
import com.re.trans_route.type.SeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TicketService {
    private static final int CANCEL_HOURS_BEFORE_DEPARTURE = 12;

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public TicketService(TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket findByTicketCodeAndPassengerPhone(String ticketCode, String passengerPhone) {
        return ticketRepository.findByTicketCodeAndPassengerPhoneWithDetails(ticketCode, passengerPhone)
                .orElse(null);
    }

    public boolean canCancel(Ticket ticket) {
        if (ticket == null || ticket.getTrip() == null) {
            return false;
        }
        if (ticket.getStatus() == BookingStatus.CANCELLED) {
            return false;
        }
        if (ticket.getStatus() != BookingStatus.PAID && ticket.getStatus() != BookingStatus.CONFIRM) {
            return false;
        }
        LocalDateTime deadline = ticket.getTrip().getDepartureTime().minusHours(CANCEL_HOURS_BEFORE_DEPARTURE);
        return LocalDateTime.now().isBefore(deadline);
    }

    @Transactional
    public Ticket cancelTicket(String ticketCode, String passengerPhone) {
        Ticket ticket = ticketRepository.findByTicketCodeAndPassengerPhoneWithDetails(ticketCode, passengerPhone)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vé"));

        if (ticket.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Vé đã được hủy trước đó");
        }
        if (ticket.getStatus() != BookingStatus.PAID && ticket.getStatus() != BookingStatus.CONFIRM) {
            throw new IllegalStateException("Vé không thể hủy ở trạng thái hiện tại");
        }
        if (!canCancel(ticket)) {
            throw new IllegalStateException(
                    "Chỉ được hủy vé trước " + CANCEL_HOURS_BEFORE_DEPARTURE + " giờ so với giờ khởi hành");
        }

        ticket.setStatus(BookingStatus.CANCELLED);
        Seat seat = ticket.getSeat();
        if (seat != null) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(seat);
        }
        return ticketRepository.save(ticket);
    }

    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAllWithTrip(pageable);
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }
}
