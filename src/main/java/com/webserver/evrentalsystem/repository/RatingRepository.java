package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

}
