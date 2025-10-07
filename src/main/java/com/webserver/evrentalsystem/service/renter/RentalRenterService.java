package com.webserver.evrentalsystem.service.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.request.*;

import java.util.List;

public interface RentalRenterService {
    List<RentalDto> getCurrentRentals();
    List<RentalCheckDto> getRentalChecks(Long rentalId);
    RentalDto returnVehicle(Long rentalId, RentalReturnRequest request);
}
