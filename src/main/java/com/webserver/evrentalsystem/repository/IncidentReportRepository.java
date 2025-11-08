package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.IncidentReport;
import com.webserver.evrentalsystem.entity.IncidentStatus;
import com.webserver.evrentalsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    boolean existsByVehicleAndStatusIn(Vehicle vehicle, List<IncidentStatus> statuses);
}
