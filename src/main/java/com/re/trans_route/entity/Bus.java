package com.re.trans_route.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, name = "total_seats")
    private Integer totalSeats;

    @Column(nullable = true, name = "assigned_route")
    private String assignedRoute;

    private String company;
    private String driver;

    private String status;
}
