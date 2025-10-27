package com.webserver.evrentalsystem.model.dto.response;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillResponse {
    private RentalDto rental;
    private BigDecimal rentalCost;
    private BigDecimal violationCost;
    private BigDecimal insurance;
    private BigDecimal totalBill;
}
