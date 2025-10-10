package com.webserver.evrentalsystem.model.dto.entitydto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RatingDto {
    private Long id;
    private RentalDto rental;
    private UserDto renter;
    private Integer rating; // 1-5
    private String comment;
    private LocalDateTime createdAt;
}
