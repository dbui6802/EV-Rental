package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Query("SELECT s FROM Station s WHERE s.status = 'active'")
    List<Station> findAllActiveStations();

    @Query("SELECT s FROM Station s WHERE s.id = :id AND s.status = 'active'")
    Station findByIdAndActiveTrue(Long id);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Station s WHERE s.name = :name")
    boolean existsByName(String name);
}
