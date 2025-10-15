package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.RentalCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RentalCheckRepository extends JpaRepository<RentalCheck, Long>, JpaSpecificationExecutor<RentalCheck> {
    List<RentalCheck> findByRentalId(Long rentalId);
}
