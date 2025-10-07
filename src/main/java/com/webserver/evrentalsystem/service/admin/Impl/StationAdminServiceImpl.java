package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.Station;
import com.webserver.evrentalsystem.entity.StationStatus;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.request.CreateStationRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateStationRequest;
import com.webserver.evrentalsystem.model.mapping.StationMapper;
import com.webserver.evrentalsystem.repository.StationRepository;
import com.webserver.evrentalsystem.service.admin.StationAdminService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StationAdminServiceImpl implements StationAdminService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public StationDto createStation(CreateStationRequest request) {
        userValidation.validateAdmin();

        if (!request.isValid()) {
            throw new InvalidateParamsException("Tham số không hợp lệ");
        }
        // check if station name already exists
        if (stationRepository.existsByName(request.getName())) {
            throw new InvalidateParamsException("Tên trạm đã tồn tại");
        }
        Station station = new Station();
        station.setName(request.getName());
        station.setAddress(request.getAddress());
        station.setLatitude(request.getLatitude());
        station.setLongitude(request.getLongitude());
        station.setStatus(StationStatus.fromValue(request.getStatus()));
        return stationMapper.toStationDto(stationRepository.save(station));
    }

    @Override
    public List<StationDto> getAllStations() {
        userValidation.validateAdmin();
        return stationRepository.findAll()
                .stream()
                .map(stationMapper::toStationDto)
                .toList();
    }

    @Override
    public StationDto getStationById(Long id) {
        userValidation.validateAdmin();
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trạm với id = " + id));
        return stationMapper.toStationDto(station);
    }

    @Override
    public StationDto updateStation(Long id, UpdateStationRequest request) {
        userValidation.validateAdmin();
        if (!request.isValid()) {
            throw new InvalidateParamsException("Tham số không hợp lệ");
        }

        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trạm với id = " + id));

        if (request.getName() != null) station.setName(request.getName());
        if (request.getAddress() != null) station.setAddress(request.getAddress());
        if (request.getStatus() != null) station.setStatus(StationStatus.fromValue(request.getStatus()));
        if (request.getLatitude() != null) station.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) station.setLongitude(request.getLongitude());

        return stationMapper.toStationDto(stationRepository.save(station));
    }

    @Override
    public void deleteStation(Long id) {
        userValidation.validateAdmin();
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trạm với id = " + id));
        stationRepository.delete(station);
    }
}
