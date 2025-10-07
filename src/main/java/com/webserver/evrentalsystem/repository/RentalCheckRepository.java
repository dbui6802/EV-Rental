package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.RentalCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RentalCheckRepository extends JpaRepository<RentalCheck, Long>, JpaSpecificationExecutor<RentalCheck> {

}
