package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    @Query("SELECT v FROM Vehicle v WHERE v.id = :id AND v.status = 'available'")
    Vehicle findByIdAndStatusAvailable(Long id);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vehicle v WHERE v.licensePlate = :licensePlate")
    boolean existsByLicensePlate(String licensePlate);
}
