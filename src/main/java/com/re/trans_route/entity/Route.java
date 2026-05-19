package com.re.trans_route.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_id", nullable = false)
    private Location fromLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_id", nullable = false)
    private Location toLocation;

    @Column(nullable = false)
    private Double distance;
}
