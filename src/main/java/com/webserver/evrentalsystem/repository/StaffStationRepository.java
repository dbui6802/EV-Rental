package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.StaffStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffStationRepository extends JpaRepository<StaffStation, Long> {
    @Query("SELECT ss FROM StaffStation ss WHERE ss.station.id = :stationId AND ss.isActive = true")
    List<StaffStation> findAllByStationId(Long stationId);

    @Query("SELECT ss FROM StaffStation ss WHERE ss.staff.id = :staffId AND ss.isActive = true")
    List<StaffStation> findAllByStaffId(Long staffId);
}
