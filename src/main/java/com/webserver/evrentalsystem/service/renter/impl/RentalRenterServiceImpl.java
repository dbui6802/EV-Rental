package com.webserver.evrentalsystem.service.renter.impl;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.request.RentalReturnRequest;
import com.webserver.evrentalsystem.repository.ReservationRepository;
import com.webserver.evrentalsystem.repository.StationRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.renter.RentalRenterService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RentalRenterServiceImpl implements RentalRenterService {

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<RentalDto> getCurrentRentals() {
        return List.of();
    }

    @Override
    public List<RentalCheckDto> getRentalChecks(Long rentalId) {
        return List.of();
    }

    @Override
    public RentalDto returnVehicle(Long rentalId, RentalReturnRequest request) {
        return null;
    }
}
