package com.re.trans_route.repository;

import com.re.trans_route.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTripId(Long tripId);

//    gaii? phong' ghe' cho ve' dang giu~ cho~ thanh toan'
    @Modifying
    @Query("UPDATE Seat s SET s.status = 'AVAILABLE' WHERE s.id IN " +
            "(SELECT t.id FROM Ticket t WHERE t.status = 'PENDING' AND t.bookingTime < :thirtyMinPrev )")
    int releaseSeatByExpiredTicket(@Param("thirtyMinPrev") LocalDateTime thirtyMinPrev);
}
