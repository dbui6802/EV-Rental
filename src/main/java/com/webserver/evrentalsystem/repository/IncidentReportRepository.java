package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.IncidentReport;
import com.webserver.evrentalsystem.entity.IncidentStatus;
import com.webserver.evrentalsystem.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    List<IncidentReport> findByRentalIn(List<Rental> rentals);
    List<IncidentReport> findByStatus(IncidentStatus status);
}
