package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    @Query("SELECT r FROM Reservation r WHERE r.renter.id = :renterId")
    List<Reservation> findByRenterId(Long renterId);

    @Query("SELECT r FROM Reservation r WHERE r.id = :id AND r.renter.id = :renterId")
    Reservation findByIdAndRenterId(Long id, Long renterId);
}
