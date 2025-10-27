package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ViolationDto;
import com.webserver.evrentalsystem.model.dto.request.*;
import com.webserver.evrentalsystem.model.dto.response.BillResponse;
import com.webserver.evrentalsystem.model.mapping.RentalCheckMapper;
import com.webserver.evrentalsystem.model.mapping.RentalMapper;
import com.webserver.evrentalsystem.model.mapping.ReservationMapper;
import com.webserver.evrentalsystem.model.mapping.ViolationMapper;
import com.webserver.evrentalsystem.repository.*;
import com.webserver.evrentalsystem.service.staff.RentalStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import com.webserver.evrentalsystem.specification.ReservationSpecification;
import com.webserver.evrentalsystem.utils.FileStorageUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Duration;
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

    @Autowired
    private ViolationRepository violationRepository;

    @Autowired
    private ViolationMapper violationMapper;

    @Value("${percent.deposit}")
    private BigDecimal percentDeposit;

    @Value("${min.deposit.of.high.risk}")
    private BigDecimal minDepositOfHighRisk;

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
        Boolean highRisk = request.getHighRisk();

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
        long hours = Duration.between(finalStartTime, finalEndTime).toHours();
        if (hours == 0) {
            hours = 1; // Thu tối thiểu 1 giờ
        } else if (Duration.between(finalStartTime, finalEndTime).toMinutes() % 60 != 0) {
            hours += 1; // Làm tròn lên nếu có phút lẻ
        }

        // Tính số tiền cọc
        BigDecimal deposit;
        BigDecimal insurance = request.getInsurance();
        BigDecimal rentalCost = vehicle.getPricePerHour().multiply(BigDecimal.valueOf(hours));
        BigDecimal totalCost = rentalCost.add(insurance);
        BigDecimal minDeposit = totalCost.multiply(percentDeposit); // Tiền cọc tối thiểu 30% tổng tiền thuê

        if (Boolean.FALSE.equals(highRisk)) {
            // Nếu khách hình thường thì chỉ cần cọc nhỏ nhật
            deposit = minDeposit;
        } else {
            // Nếu khách high risk
            if (minDeposit.compareTo(minDepositOfHighRisk) < 0) {
                // Nếu số tiền cọc nhỏ nhất < số tiền cọc tối thiếu => Lấy số tiền cọc tối thiểu
                deposit = minDepositOfHighRisk;
            } else {
                // Nếu số tiền cọc nhỏ nhất >= số tiền cọc tối thiếu => Lấy số tiền cọc nhỏ nhất  + số tiền cọc tối thiểu
                deposit = minDeposit.add(minDepositOfHighRisk);
            }
        }

        Rental rental = new Rental();
        rental.setRenter(renter);
        rental.setVehicle(vehicle);
        rental.setStationPickup(vehicle.getStation());
        rental.setStaffPickup(staff);
        rental.setStartTime(finalStartTime);
        rental.setEndTime(finalEndTime);
        rental.setTotalCost(totalCost);
        rental.setRentalCost(rentalCost);
        rental.setInsurance(insurance);
        rental.setRentalType(rentalType);
        rental.setDepositAmount(deposit);
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

        // Cập nhật trạng thái xe thành RENTED, phần trăm pin xe hiện tại
        Vehicle vehicle = rental.getVehicle();
        vehicle.setOdo(request.getOdo());
        vehicle.setBatteryLevel(request.getBatteryLevel());
        vehicle.setStatus(VehicleStatus.RENTED);
        vehicleRepository.save(vehicle);

        // Cập nhật trạng thái rental thành WAIT_CONFIRM
        rental.setOdoStart(request.getOdo());
        rental.setBatteryLevelStart(request.getBatteryLevel());
        rental.setStatus(RentalStatus.WAIT_CONFIRM);
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

    @Override
    public RentalCheckDto confirmReturn(ConfirmRentalRequest request, MultipartFile photo, MultipartFile staffSignature, MultipartFile customerSignature) {
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
        // check type must be "return"
        CheckType checkType;
        try {
            checkType = CheckType.valueOf(request.getCheckType().toUpperCase());
            if (checkType != CheckType.RETURN) {
                throw new InvalidateParamsException("checkType phải là 'return'");
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidateParamsException("checkType phải là 'return'");
        }

        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Lượt thuê (rental) không tồn tại");
        }
        if (rental.getStatus() != RentalStatus.IN_USE) {
            throw new ConflictException("Chỉ có thể xác nhận trả xe khi lượt thuê ở trạng thái IN_USE");
        }

        // Cập nhật trạng thái xe thành RENTED
        Vehicle vehicle = rental.getVehicle();
        vehicle.setOdo(request.getOdo());
        vehicle.setBatteryLevel(request.getBatteryLevel());
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);

        // Cập nhật trạng thái rental thành WAITING_FOR_PAYMENT
        rental.setOdoEnd(request.getOdo());
        rental.setBatteryLevelEnd(request.getBatteryLevel());
        rental.setStatus(RentalStatus.WAITING_FOR_PAYMENT);
        rental.setStaffReturn(staff);
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

    @Override
    public ViolationDto addViolation(ViolationRequest request) {
        User staff = userValidation.validateStaff();

        Rental rental = rentalRepository.findById(request.getRentalId()).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Lượt thuê (rental) không tồn tại");
        }
        if (rental.getStatus() != RentalStatus.WAIT_CONFIRM
                && rental.getStatus() != RentalStatus.IN_USE
                && rental.getStatus() != RentalStatus.WAITING_FOR_PAYMENT) {
            throw new ConflictException("Chỉ có thể thêm chi phí phát sinh cho lượt thuê ở trạng thái WAIT_CONFIRM, IN_USE hoặc WAITING_FOR_PAYMENT");
        }

        Violation violation = new Violation();
        violation.setRental(rental);
        violation.setStaff(staff);
        violation.setDescription(request.getDescription());
        violation.setFineAmount(request.getFineAmount());
        violation.setCreatedAt(LocalDateTime.now());

        return violationMapper.toViolationDto(violationRepository.save(violation));
    }

    @Override
    public List<ViolationDto> getViolationsByRentalId(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Lượt thuê (rental) không tồn tại");
        }

        List<Violation> violations = violationRepository.findByRentalId(rentalId);
        return violations.stream()
                .map(violationMapper::toViolationDto)
                .toList();
    }

    @Override
    public BillResponse calculateBill(Long rentalId, BillRequest request) {
        userValidation.validateStaff();
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Rental không tồn tại");
        }
        if (rental.getStatus() != RentalStatus.WAITING_FOR_PAYMENT) {
            throw new ConflictException("Chỉ có thể tính bill cho rental đang ở trạng thái WAITING_FOR_PAYMENT");
        }

        Vehicle vehicle = rental.getVehicle();

        // Thời gian thuê
        LocalDateTime start = rental.getStartTime();
        LocalDateTime end = request.getReturnTime();
        if (end.isBefore(start)) {
            throw new InvalidateParamsException("Thời gian trả xe không hợp lệ");
        }

        long hours = Duration.between(start, end).toHours();
        if (hours == 0) hours = 1; // tối thiểu 1h

        BigDecimal insurance = rental.getInsurance();
        BigDecimal rentalCost = vehicle.getPricePerHour().multiply(BigDecimal.valueOf(hours));

        // Tổng violation
        List<Violation> violations = violationRepository.findByRentalId(rentalId);
        BigDecimal violationCost = violations.stream()
                .map(v -> v.getFineAmount() != null ? v.getFineAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tổng bill
        BigDecimal totalBill = rentalCost.add(violationCost).subtract(insurance);

        // Cập nhật thời gian trả xe, tổng tiền thuê
        rental.setEndTime(end);
        rental.setTotalCost(rentalCost);
        rentalRepository.save(rental);

        return BillResponse.builder()
                .rental(rentalMapper.toRentalDto(rental))
                .rentalCost(rentalCost)
                .violationCost(violationCost)
                .insurance(insurance)
                .totalBill(totalBill)
                .build();
    }

    @Override
    public void confirmPayment(Long rentalId) {
        userValidation.validateStaff();
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Rental không tồn tại");
        }

        if (rental.getStatus() != RentalStatus.WAITING_FOR_PAYMENT) {
            throw new ConflictException("Chỉ có thể xác nhận thanh toán cho rental đang ở trạng thái WAITING_FOR_PAYMENT");
        }

        // Cập nhật rental
        rental.setStatus(RentalStatus.RETURNED);

        rentalRepository.save(rental);
    }

    @Override
    public RentalDto returnDeposit(Long rentalId) {
        userValidation.validateStaff();
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            throw new NotFoundException("Lượt thuê (rental) không tồn tại");
        }
        if (rental.getStatus() != RentalStatus.RETURNED) {
            throw new ConflictException("Chỉ có thể trả tiền cọc cho lượt thuê ở trạng thái RETURNED");
        }
        if (rental.getDepositAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("Lượt thuê không có tiền cọc để trả");
        }
        if (rental.getDepositStatus() == DepositStatus.REFUNDED) {
            throw new ConflictException("Tiền cọc đã được trả trước đó");
        }
        rental.setDepositStatus(DepositStatus.REFUNDED);
        return rentalMapper.toRentalDto(rentalRepository.save(rental));
    }
}
