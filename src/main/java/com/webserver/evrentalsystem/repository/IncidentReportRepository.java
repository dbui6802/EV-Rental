package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {

}
