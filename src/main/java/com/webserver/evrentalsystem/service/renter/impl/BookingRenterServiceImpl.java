package com.webserver.evrentalsystem.service.renter.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.CreateReservationRequest;
import com.webserver.evrentalsystem.model.mapping.ReservationMapper;
import com.webserver.evrentalsystem.model.mapping.StationMapper;
import com.webserver.evrentalsystem.model.mapping.VehicleMapper;
import com.webserver.evrentalsystem.repository.ReservationRepository;
import com.webserver.evrentalsystem.repository.StationRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.renter.BookingRenterService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import com.webserver.evrentalsystem.specification.ReservationSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.webserver.evrentalsystem.specification.VehicleSpecification.*;

@Service
@Transactional
public class BookingRenterServiceImpl implements BookingRenterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public List<StationDto> getStations() {
        return stationRepository.findAllActiveStations().stream()
                .map(stationMapper::toStationDto)
                .toList();
    }

    @Override
    public List<VehicleDto> getVehicles(String type, Long stationId, Double priceMin, Double priceMax) {
        if (type != null) {
            try {
                VehicleType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidateParamsException("Loại xe không hợp lệ. Chỉ chấp nhận 'motorbike' hoặc 'car'.");
            }
        }


        List<Vehicle> vehicles = vehicleRepository.findAll(
                Specification.where(isAvailable())
                        .and(hasType(type == null ? null : VehicleType.valueOf(type.toUpperCase())))
                        .and(hasStation(stationId))
                        .and(priceGreaterOrEqual(priceMin))
                        .and(priceLessOrEqual(priceMax))
        );
        return vehicles.stream()
                .map(vehicleMapper::toVehicleDto)
                .toList();
    }

    @Override
    public ReservationDto createReservation(CreateReservationRequest request) {
        User renter = userValidation.validateRenter();
        Long vehicleId = request.getVehicleId();
        LocalDateTime reservedStartTime = request.getReservedStartTime();
        LocalDateTime reservedEndTime = request.getReservedEndTime();

        // Validate input
        if (vehicleId == null) {
            throw new InvalidateParamsException("vehicleId không được để trống.");
        }
        if (reservedStartTime == null || reservedEndTime == null) {
            throw new InvalidateParamsException("reservedStartTime và reservedEndTime không được để trống.");
        }
        // Check vehicle exists and is available at the station
        Vehicle vehicle = vehicleRepository.findByIdAndStatusAvailable(vehicleId);
        if (vehicle == null) {
            throw new NotFoundException("Xe không tồn tại hoặc không khả dụng tại trạm.");
        }
        // Check station exists
        Station station = stationRepository.findByIdAndActiveTrue(vehicle.getStation().getId());
        if (station == null) {
            throw new NotFoundException("Trạm không tồn tại hoặc không hoạt động.");
        }
        // Check time validity
        if (reservedEndTime.isBefore(reservedStartTime) || reservedEndTime.isEqual(reservedStartTime)) {
            throw new ConflictException("Thời gian kết thúc phải sau thời gian bắt đầu.");
        }
        if (reservedStartTime.isBefore(LocalDateTime.now())) {
            throw new ConflictException("Thời gian bắt đầu phải lớn hơn hiện tại");
        }

        // mark vehicle as not available
        vehicle.setStatus(VehicleStatus.RESERVED);
        vehicleRepository.save(vehicle);
        // create reservation
        Reservation reservation = new Reservation();
        reservation.setRenter(renter);
        reservation.setVehicle(vehicle);
        reservation.setReservedStartTime(reservedStartTime);
        reservation.setReservedEndTime(reservedEndTime);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setInsurance(request.getInsurance());
        Reservation saved = reservationRepository.save(reservation);

        return reservationMapper.toReservationDto(saved);
    }

    @Override
    public List<ReservationDto> getMyReservations(ReservationStatus status, Long vehicleId, LocalDateTime startFrom, LocalDateTime startTo) {
        User renter = userValidation.validateRenter();

        Specification<Reservation> spec = Specification.where(ReservationSpecification.hasRenter(renter.getId()))
                .and(ReservationSpecification.hasStatus(status))
                .and(ReservationSpecification.hasVehicle(vehicleId))
                .and(ReservationSpecification.startFrom(startFrom))
                .and(ReservationSpecification.startTo(startTo));

        List<Reservation> reservations = reservationRepository.findAll(spec);
        return reservations.stream()
                .map(reservationMapper::toReservationDto)
                .toList();
    }

    @Override
    public ReservationDto getReservationById(Long id) {
        User renter = userValidation.validateRenter();
        Reservation reservation = reservationRepository.findByIdAndRenterId(id, renter.getId());
        if (reservation == null) {
            throw new NotFoundException("Đặt xe không tồn tại.");
        }
        return reservationMapper.toReservationDto(reservation);
    }

    @Override
    public void cancelReservation(Long id, String cancelReason) {
        User renter = userValidation.validateRenter();
        Reservation reservation = reservationRepository.findByIdAndRenterId(id, renter.getId());
        if (reservation == null) {
            throw new NotFoundException("Đặt xe không tồn tại.");
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ConflictException("Đặt xe đã bị hủy.");
        }
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new ConflictException("Đặt xe đã được xác nhận, không thể hủy.");
        }
        // mark reservation as cancelled
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledBy(renter);
        reservation.setCancelledReason(cancelReason);
        reservationRepository.save(reservation);
        // mark vehicle as available
        Vehicle vehicle = reservation.getVehicle();
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);
    }
}
