package com.re.trans_route.repository;

import com.re.trans_route.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByTicketCodeAndPassengerPhone(String ticketCode, String passengerPhone);

    @Query(value = "SELECT t FROM Ticket t JOIN FETCH t.trip trip JOIN FETCH trip.route r JOIN FETCH r.fromLocation JOIN FETCH r.toLocation",
           countQuery = "SELECT COUNT(t) FROM Ticket t")
    Page<Ticket> findAllWithTrip(Pageable pageable);
}

