package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Complaint;
import com.webserver.evrentalsystem.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    @Query("SELECT c FROM Complaint c WHERE (:status IS NULL OR c.status = :status)")
    List<Complaint> findByStatus(ComplaintStatus status);
}
