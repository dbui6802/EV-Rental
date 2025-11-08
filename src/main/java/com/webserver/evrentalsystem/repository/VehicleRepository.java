package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    @Query("SELECT v FROM Vehicle v WHERE v.id = :id AND v.status = 'available'")
    Vehicle findByIdAndStatusAvailable(Long id);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vehicle v WHERE v.licensePlate = :licensePlate")
    boolean existsByLicensePlate(String licensePlate);

    List<Vehicle> findByStationId(Long stationId);

    boolean existsByStationIdAndStatusIn(Long stationId, List<VehicleStatus> statuses);

    boolean existsByStationId(Long stationId);

    Optional<Vehicle> findByLicensePlateIgnoreCase(String licensePlate);
}

