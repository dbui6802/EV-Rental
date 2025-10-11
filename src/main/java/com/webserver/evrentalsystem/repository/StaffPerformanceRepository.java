package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.response.StaffPerformanceResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffPerformanceRepository extends CrudRepository<User, Long> {

    @Query("""
                SELECT new com.webserver.evrentalsystem.model.dto.response.StaffPerformanceResponse(
                    s.id,
                    s.fullName,
                    (SELECT COUNT(r1.id) FROM Rental r1 WHERE r1.staffPickup = s),
                    (SELECT COUNT(r2.id) FROM Rental r2 WHERE r2.staffReturn = s),
                    (SELECT COUNT(c.id) FROM Complaint c WHERE c.staff = s),
                    (SELECT COALESCE(AVG(sr.rating), 0) FROM StaffRating sr WHERE sr.staff = s)
                )
                FROM User s
                WHERE s.role = 'STAFF'
            """)
    List<StaffPerformanceResponse> getAllStaffPerformance();
}
