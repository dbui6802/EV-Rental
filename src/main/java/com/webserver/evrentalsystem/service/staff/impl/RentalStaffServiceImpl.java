package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.request.ConfirmRentalRequest;
import com.webserver.evrentalsystem.model.dto.request.RentalCheckInRequest;
import com.webserver.evrentalsystem.model.dto.request.ReservationFilterRequest;
import com.webserver.evrentalsystem.model.mapping.RentalCheckMapper;
import com.webserver.evrentalsystem.model.mapping.RentalMapper;
import com.webserver.evrentalsystem.model.mapping.ReservationMapper;
import com.webserver.evrentalsystem.repository.*;
import com.webserver.evrentalsystem.service.staff.RentalStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import com.webserver.evrentalsystem.specification.ReservationSpecification;
import com.webserver.evrentalsystem.utils.FileStorageUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class RentalStaffServiceImpl implements RentalStaffService {

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private RentalCheckRepository rentalCheckRepository;

    @Autowired
    private RentalCheckMapper rentalCheckMapper;

    public List<ReservationDto> getReservations(ReservationFilterRequest filter) {
        List<Reservation> reservations = reservationRepository.findAll(
                Specification.where(ReservationSpecification.hasRenter(filter.getRenterId()))
                        .and(ReservationSpecification.hasVehicle(filter.getVehicleId()))
                        .and(ReservationSpecification.hasStatus(ReservationStatus.fromValue(filter.getStatus())))
                        .and(ReservationSpecification.startFrom(filter.getStartFrom()))
                        .and(ReservationSpecification.startTo(filter.getStartTo()))
        );

        return reservations.stream()
                .map(reservationMapper::toReservationDto)
                .toList();
    }

    public List<RentalDto> getRentals(Long renterId, Long vehicleId, Long stationPickupId,
                                      Long stationReturnId, String status,
                                      LocalDateTime startFrom, LocalDateTime startTo) {
        Specification<Rental> spec = Specification.where(null);

        if (renterId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("renter").get("id"), renterId));
        }
        if (vehicleId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("vehicle").get("id"), vehicleId));
        }
        if (stationPickupId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("stationPickup").get("id"), stationPickupId));
        }
        if (stationReturnId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("stationReturn").get("id"), stationReturnId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), RentalStatus.valueOf(status.toUpperCase())));
        }
        if (startFrom != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startTime"), startFrom));
        }
        if (startTo != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("startTime"), startTo));
        }

        return rentalRepository.findAll(spec).stream()
                .map(rentalMapper::toRentalDto)
                .toList();
    }

    @Override
    public RentalDto checkIn(RentalCheckInRequest request) {
        User staff = userValidation.validateStaff();
        Long renterId = request.getRenterId();
        Long reservationId = request.getReservationId();
        Long vehicleId = request.getVehicleId();
        Long stationId = request.getStationId();
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        BigDecimal depositAmount = request.getDepositAmount();

        User renter = userRepository.findById(renterId).orElse(null);
        if (renter == null) {
            throw new NotFoundException("Người thuê (renter) không tồn tại");
        }

        if (reservationId == null && vehicleId == null) {
            throw new InvalidateParamsException("Phải cung cấp reservationId hoặc vehicleId");
        }

        if (reservationId != null && vehicleId != null) {
            throw new InvalidateParamsException("Chỉ được cung cấp reservationId hoặc vehicleId");
        }

        if (depositAmount != null && depositAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidateParamsException("Tiền cọc phải lớn hơn hoặc bằng 0");
        }

        Vehicle vehicle;
        LocalDateTime finalStartTime;
        LocalDateTime finalEndTime;
        RentalType rentalType;
        if (reservationId != null) {
            // Đã book trước
            Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                throw new ConflictException("Reservation không tồn tại");
            }
            // Kiểm tra reservation thuộc về renter
            if (!reservation.getRenter().getId().equals(renter.getId())) {
                throw new ConflictException("Reservation không thuộc về người dùng");
            }
            // Kiểm tra reservation ở trạng thái PENDING
            if (reservation.getStatus() != ReservationStatus.PENDING) {
                throw new ConflictException("Reservation không ở trạng thái PENDING nên có thể đã được check-in hoặc hủy hoặc hết hạn");
            }
            // Cập nhật trạng thái reservation thành CONFIRMED
            reservation.setStatus(ReservationStatus.CONFIRMED);
            vehicle = reservation.getVehicle();
            finalStartTime = reservation.getReservedStartTime();
            finalEndTime = reservation.getReservedEndTime();
            rentalType = RentalType.BOOKING;
        } else {
            if (startTime == null || endTime == null) {
                throw new InvalidateParamsException("Phải cung cấp startTime, endTime và depositAmount khi thuê walk-in");
            }
            // Đến thuê trực tiếp (walk-in)
            vehicle = vehicleRepository.findById(vehicleId).orElse(null);
            if (vehicle == null) {
                throw new ConflictException("Xe không tồn tại");
            }
            finalStartTime = startTime;
            finalEndTime = endTime;
            rentalType = RentalType.WALK_IN;
        }

        // Kiểm tra xe
        if (!Objects.equals(vehicle.getStation().getId(), stationId)) {
            throw new ConflictException("Phải đến đúng trạm đã chọn khi đặt trước để nhận xe");
        }
        // Kiểm tra xe ở trạng thái AVAILABLE hoặc RESERVED
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE && vehicle.getStatus() != VehicleStatus.RESERVED) {
            throw new ConflictException("Xe không ở trạng thái AVAILABLE hoặc RESERVED");
        }

        // Tính tiền thuê dự tính dựa trên thời gian thuê
        if (finalEndTime.isBefore(finalStartTime) || finalEndTime.isEqual(finalStartTime)) {
            throw new InvalidateParamsException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }
        long hours = java.time.Duration.between(finalStartTime, finalEndTime).toHours();
        if (hours == 0) {
            hours = 1; // Thu tối thiểu 1 giờ
        } else if (java.time.Duration.between(finalStartTime, finalEndTime).toMinutes() % 60 != 0) {
            hours += 1; // Làm tròn lên nếu có phút lẻ
        }
        BigDecimal rentalCost = vehicle.getPricePerHour().multiply(BigDecimal.valueOf(hours));
        // Kiểm tra tiền cọc
        BigDecimal minDeposit = rentalCost.multiply(BigDecimal.valueOf(0.3)); // Tiền cọc tối thiểu 30% tổng tiền thuê
        if (depositAmount != null && depositAmount.compareTo(minDeposit) < 0) {
            throw new ConflictException("Tiền cọc phải lớn hơn hoặc bằng 30% tổng tiền thuê là " +minDeposit);
        }

        Rental rental = new Rental();
        rental.setRenter(renter);
        rental.setVehicle(vehicle);
        rental.setStationPickup(vehicle.getStation());
        rental.setStaffPickup(staff);
        rental.setStartTime(finalStartTime);
        rental.setEndTime(finalEndTime);
        rental.setTotalCost(rentalCost);
        rental.setRentalType(rentalType);
        rental.setDepositAmount(depositAmount != null ? depositAmount : BigDecimal.ZERO);
        rental.setDepositStatus(DepositStatus.PENDING);
        rental.setStatus(RentalStatus.BOOKED);
        rental.setCreatedAt(LocalDateTime.now());

        return rentalMapper.toRentalDto(rentalRepository.save(rental));
    }

    @Override
    public RentalDto cancelRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Lượt thuê (rental) không tồn tại");
        }
        if (rental.getStatus() != RentalStatus.BOOKED) {
            throw new ConflictException("Chỉ có thể hủy lượt thuê ở trạng thái BOOKED");
        }
        rental.setStatus(RentalStatus.CANCELLED);
        return rentalMapper.toRentalDto(rentalRepository.save(rental));
    }

    @Override
    public RentalDto holdDeposit(Long rentalId) {
        userValidation.validateStaff();
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Lượt thuê (rental) không tồn tại");
        }
        if (rental.getStatus() != RentalStatus.BOOKED) {
            throw new ConflictException("Chỉ có thể giữ tiền cọc cho lượt thuê ở trạng thái BOOKED");
        }
        if (rental.getDepositAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("Lượt thuê chưa đặt cọc");
        }
        if (rental.getDepositStatus() == DepositStatus.HELD) {
            throw new ConflictException("Tiền cọc đã được giữ trước đó");
        }
        rental.setDepositStatus(DepositStatus.HELD);
        return rentalMapper.toRentalDto(rentalRepository.save(rental));
    }

    @Override
    public RentalCheckDto confirmPickup(ConfirmRentalRequest request, MultipartFile photo, MultipartFile staffSignature, MultipartFile customerSignature) {
        User staff = userValidation.validateStaff();
        Long rentalId = request.getRentalId();
        String conditionReport = request.getConditionReport();

        if (photo == null || photo.isEmpty()) {
            throw new InvalidateParamsException("photo không được để trống");
        }
        if (staffSignature == null || staffSignature.isEmpty()) {
            throw new InvalidateParamsException("staffSignature không được để trống");
        }
        if (customerSignature == null || customerSignature.isEmpty()) {
            throw new InvalidateParamsException("customerSignature không được để trống");
        }
        // check type must be "pickup"
        CheckType checkType;
        try {
            checkType = CheckType.valueOf(request.getCheckType().toUpperCase());
            if (checkType != CheckType.PICKUP) {
                throw new InvalidateParamsException("checkType phải là 'pickup'");
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidateParamsException("checkType phải là 'pickup'");
        }

        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Lượt thuê (rental) không tồn tại");
        }
        if (rental.getStatus() != RentalStatus.BOOKED) {
            throw new ConflictException("Chỉ có thể xác nhận giao xe cho khách khi lượt thuê ở trạng thái BOOKED");
        }
        if (rental.getDepositStatus() != DepositStatus.HELD && rental.getDepositAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new ConflictException("Chỉ có thể xác nhận giao xe cho khách khi tiền cọc đã được thanh toán");
        }

        // Cập nhật trạng thái xe thành RENTED
        Vehicle vehicle = rental.getVehicle();
        vehicle.setStatus(VehicleStatus.RENTED);
        vehicleRepository.save(vehicle);

        // Cập nhật trạng thái rental thành IN_USE
        rental.setStatus(RentalStatus.IN_USE);
        rentalRepository.save(rental);

        // Lưu biên bản giao xe
        RentalCheck rentalCheck = new RentalCheck();
        rentalCheck.setRental(rental);
        rentalCheck.setStaff(staff);
        rentalCheck.setCheckType(checkType);
        rentalCheck.setConditionReport(conditionReport);
        // Lưu file ảnh và chữ ký lên server hoặc dịch vụ lưu trữ file, sau đó lấy URL
        String photoUrl;
        String staffSignatureUrl;
        String customerSignatureUrl;
        try {
            photoUrl = FileStorageUtils.saveFile(photo);
            staffSignatureUrl = FileStorageUtils.saveFile(staffSignature);
            customerSignatureUrl = FileStorageUtils.saveFile(customerSignature);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu file", e);
        }
        rentalCheck.setPhotoUrl(photoUrl);
        rentalCheck.setStaffSignatureUrl(staffSignatureUrl);
        rentalCheck.setCustomerSignatureUrl(customerSignatureUrl);
        rentalCheck.setCreatedAt(LocalDateTime.now());

        return rentalCheckMapper.toRentalCheckDto(rentalCheckRepository.save(rentalCheck));
    }
}
