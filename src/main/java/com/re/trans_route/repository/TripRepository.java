package com.re.trans_route.repository;

import com.re.trans_route.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Page<Trip> findAll(Pageable pageable);

    @Query("""
        SELECT t
        FROM Trip t
        WHERE t.route.id = :routeId
          AND t.departureTime >= :startOfDay
          AND t.departureTime < :endOfDay
    """)
    List<Trip> findByRouteAndDateRange(Long routeId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    /**
     * Tìm chuyến theo tên điểm đi/đến (qua bảng routes + locations) và ngày khởi hành.
     * Trip không lưu from/to — chỉ join route_id → routes.from_id / to_id.
     */
    @Query("""
        SELECT DISTINCT t
        FROM Trip t
        JOIN FETCH t.route r
        JOIN FETCH r.fromLocation fl
        JOIN FETCH r.toLocation tl
        JOIN FETCH t.bus b
        WHERE fl.name = :fromPlace
          AND tl.name = :toPlace
          AND t.departureTime >= :startOfDay
          AND t.departureTime < :endOfDay
        ORDER BY t.departureTime ASC
    """)
    List<Trip> searchByPlacesAndDate(String fromPlace, String toPlace,
                                     LocalDateTime startOfDay, LocalDateTime endOfDay);
}
