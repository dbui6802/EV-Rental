package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Error;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends JpaRepository<Error, Long> {
    @Query("SELECT e FROM Error e WHERE e.message = ?1")
    Error findByErrorMessage(String errorMessage);
}
