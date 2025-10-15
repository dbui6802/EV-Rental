package com.webserver.evrentalsystem.service.global.impl;

import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.entity.VehicleType;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.mapping.StationMapper;
import com.webserver.evrentalsystem.model.mapping.VehicleMapper;
import com.webserver.evrentalsystem.repository.StationRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.global.PublicService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.webserver.evrentalsystem.specification.VehicleSpecification.*;
import static com.webserver.evrentalsystem.specification.VehicleSpecification.priceGreaterOrEqual;
import static com.webserver.evrentalsystem.specification.VehicleSpecification.priceLessOrEqual;

@Service
@Transactional
public class PublicServiceImpl implements PublicService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

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
}
