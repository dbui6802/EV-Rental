package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.entity.ReservationStatus;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    @Query("SELECT r FROM Reservation r WHERE r.renter.id = :renterId")
    List<Reservation> findByRenterId(Long renterId);

    @Query("SELECT r FROM Reservation r WHERE r.id = :id AND r.renter.id = :renterId")
    Reservation findByIdAndRenterId(Long id, Long renterId);

    List<Reservation> findAllByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime time);

    boolean existsByRenterIdAndStatusNotIn(Long renterId, List<ReservationStatus> statuses);
    boolean existsByRenterAndStatusIn(User renter, List<ReservationStatus> statuses);
}
