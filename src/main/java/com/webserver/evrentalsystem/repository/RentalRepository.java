package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {
    List<Rental> findByRenterId(Long renterId);

    boolean existsByRenterIdAndStatusNotIn(Long renterId, List<RentalStatus> statuses);

    @Query("""
        SELECT COALESCE(SUM(r.totalCost), 0)
        FROM Rental r
        WHERE r.status = 'returned'
        AND (:stationId IS NULL OR r.stationReturn.id = :stationId)
        AND r.endTime BETWEEN :startDate AND :endDate
    """)
    BigDecimal findTotalRevenue(LocalDateTime startDate, LocalDateTime endDate, Long stationId);

    @Query("""
        SELECT r.stationReturn.name, SUM(r.totalCost)
        FROM Rental r
        WHERE r.status = 'returned'
        AND (:stationId IS NULL OR r.stationReturn.id = :stationId)
        AND r.endTime BETWEEN :startDate AND :endDate
        GROUP BY r.stationReturn.name
    """)
    List<Object[]> findRevenueByStation(LocalDateTime startDate, LocalDateTime endDate, Long stationId);
}

