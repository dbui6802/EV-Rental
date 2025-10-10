package com.webserver.evrentalsystem.model.dto.entitydto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViolationDto {
    private Long id;
    private RentalDto rental;
    private UserDto staff;
    private String description;
    private BigDecimal fineAmount;
    private LocalDateTime createdAt;
}
