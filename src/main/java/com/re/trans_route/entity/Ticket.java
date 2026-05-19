package com.re.trans_route.entity;

import com.re.trans_route.type.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;

    @Column(name = "passenger_phone", nullable = false)
    private String passengerPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="trip_id", nullable=false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seat_id", nullable=false)
    private Seat seat;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name="ticket_code", unique=true, nullable = false)
    private String ticketCode;

    @Column(name="booking_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime bookingTime;
}
