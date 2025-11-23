package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.RentalStatus;
import com.webserver.evrentalsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {
    List<Rental> findByRenterId(Long renterId);

    boolean existsByRenterIdAndStatusNotIn(Long renterId, List<RentalStatus> statuses);

    boolean existsByRenterAndStatusIn(User renter, List<RentalStatus> statuses);

    @Query("SELECT r FROM Rental r WHERE r.renter.id = :userId AND r.status = 'IN_USE'")
    Optional<Rental> findActiveRentalByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(r.rentalCost + r.insurance), 0)
        FROM Rental r
        WHERE r.status = :status
        AND r.stationReturn IS NOT NULL
        AND (:stationId IS NULL OR r.stationReturn.id = :stationId)
        AND r.endTime BETWEEN :startDate AND :endDate
    """)
    BigDecimal findTotalRevenue(LocalDateTime startDate, LocalDateTime endDate, Long stationId, RentalStatus status);

    @Query("""
        SELECT r.stationReturn.name, SUM(r.rentalCost + r.insurance)
        FROM Rental r
        WHERE r.status = :status
        AND r.stationReturn IS NOT NULL
        AND (:stationId IS NULL OR r.stationReturn.id = :stationId)
        AND r.endTime BETWEEN :startDate AND :endDate
        GROUP BY r.stationReturn.name
    """)
    List<Object[]> findRevenueByStation(LocalDateTime startDate, LocalDateTime endDate, Long stationId, RentalStatus status);
}
