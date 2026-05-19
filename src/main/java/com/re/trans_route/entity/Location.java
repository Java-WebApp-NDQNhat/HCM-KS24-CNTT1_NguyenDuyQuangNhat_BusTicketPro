package com.re.trans_route.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @OneToMany(mappedBy = "fromLocation", cascade = CascadeType.ALL)
    private List<Route> fromRoutes;

    @OneToMany(mappedBy = "toLocation", cascade = CascadeType.ALL)
    private List<Route> toRoutes;
}
