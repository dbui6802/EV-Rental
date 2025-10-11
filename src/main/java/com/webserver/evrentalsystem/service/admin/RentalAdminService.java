package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalAdminService {
    List<RentalDto> getRentals(Long renterId, Long vehicleId, Long stationPickupId, Long stationReturnId, String status, LocalDateTime startFrom, LocalDateTime startTo);
}
