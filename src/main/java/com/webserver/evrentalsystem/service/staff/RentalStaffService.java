package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.request.ConfirmRentalRequest;
import com.webserver.evrentalsystem.model.dto.request.RentalCheckInRequest;
import com.webserver.evrentalsystem.model.dto.request.ReservationFilterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalStaffService {
    List<ReservationDto> getReservations(ReservationFilterRequest filter);
    List<RentalDto> getRentals(Long renterId, Long vehicleId, Long stationPickupId, Long stationReturnId, String status, LocalDateTime startFrom, LocalDateTime startTo);
    RentalDto checkIn(RentalCheckInRequest request);
    RentalDto cancelRental(Long rentalId);
    RentalDto holdDeposit(Long rentalId);
    RentalCheckDto confirmPickup(ConfirmRentalRequest request, MultipartFile photo, MultipartFile staffSignature, MultipartFile customerSignature);
}
