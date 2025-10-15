package com.webserver.evrentalsystem.service.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;

import java.util.List;

public interface RentalRenterService {
    List<RentalDto> getAllRentalsOfRenter(String status, String fromDate, String toDate);
    List<RentalCheckDto> getRentalChecks(Long rentalId);
}
