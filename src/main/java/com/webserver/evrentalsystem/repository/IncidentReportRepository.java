package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.IncidentReport;
import com.webserver.evrentalsystem.entity.IncidentStatus;
import com.webserver.evrentalsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long>, JpaSpecificationExecutor<IncidentReport> {
    boolean existsByVehicleAndStatusIn(Vehicle vehicle, List<IncidentStatus> statuses);
}
