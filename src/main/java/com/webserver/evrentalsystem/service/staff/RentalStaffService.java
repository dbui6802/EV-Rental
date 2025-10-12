package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ViolationDto;
import com.webserver.evrentalsystem.model.dto.request.*;
import com.webserver.evrentalsystem.model.dto.response.BillResponse;
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
    RentalCheckDto confirmReturn(ConfirmRentalRequest request, MultipartFile photo, MultipartFile staffSignature, MultipartFile customerSignature);
    ViolationDto addViolation(ViolationRequest request);
    List<ViolationDto> getViolationsByRentalId(Long rentalId);
    BillResponse calculateBill(Long rentalId, BillRequest request);
    void confirmPayment(Long rentalId);
    RentalDto returnDeposit(Long rentalId);
}
