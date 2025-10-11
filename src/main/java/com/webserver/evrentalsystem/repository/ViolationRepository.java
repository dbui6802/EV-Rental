package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Violation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViolationRepository extends JpaRepository<Violation, Long> {
    @Query("SELECT v FROM Violation v WHERE v.rental.id = :rentalId")
    List<Violation> findByRentalId(Long rentalId);

    @Query("SELECT v FROM Violation v WHERE v.rental.renter.id = :renterId")
    List<Violation> findByRentalRenterId(Long renterId);
}
