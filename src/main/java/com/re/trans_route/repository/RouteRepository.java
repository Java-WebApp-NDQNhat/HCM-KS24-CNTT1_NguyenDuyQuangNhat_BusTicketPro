package com.re.trans_route.repository;

import com.re.trans_route.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Page<Route> findAll(Pageable pageable);

    @Query("""
                select r
                from Route r
                where r.fromLocation.name = :fromPlace
                  and r.toLocation.name = :toPlace
            """)
    Optional<Route> findRouteByPlaceNames(String fromPlace, String toPlace);
}
