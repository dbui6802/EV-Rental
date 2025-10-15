package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffStationDto;
import com.webserver.evrentalsystem.model.dto.request.AssignStaffRequest;
import com.webserver.evrentalsystem.model.mapping.StaffStationMapper;
import com.webserver.evrentalsystem.repository.StaffStationRepository;
import com.webserver.evrentalsystem.repository.StationRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.admin.StaffStationAdminService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffStationAdminServiceImpl implements StaffStationAdminService {

    @Autowired
    private StaffStationRepository staffStationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StaffStationMapper staffStationMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public StaffStationDto assignStaffToStation(AssignStaffRequest request) {
        userValidation.validateAdmin();

        if (request == null || request.getStaffId() == null || request.getStationId() == null) {
            throw new InvalidateParamsException("staff_id và station_id là bắt buộc");
        }

        User staff = userRepository.findById(request.getStaffId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user (staff) với id = " + request.getStaffId()));

        // ensure the user is STAFF role
        if (staff.getRole() == null || staff.getRole() != Role.STAFF) {
            throw new InvalidateParamsException("User được chọn không phải là nhân viên (staff)");
        }

        Station station = stationRepository.findById(request.getStationId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy station với id = " + request.getStationId()));

        if (station.getStatus() == null || station.getStatus() != StationStatus.ACTIVE) {
            throw new InvalidateParamsException("Không thể gán nhân viên vào trạm không active");
        }

        // Check if staff already assigned to ANY active station
        boolean alreadyAssigned = staffStationRepository.findAllByStaffId(staff.getId())
                .stream()
                .anyMatch(s -> Boolean.TRUE.equals(s.getIsActive()));

        if (alreadyAssigned) {
            throw new InvalidateParamsException("Nhân viên đã được phân công ở một trạm khác và chưa kết thúc");
        }

        StaffStation ss = new StaffStation();
        ss.setStaff(staff);
        ss.setStation(station);
        ss.setAssignedAt(LocalDateTime.now());
        ss.setIsActive(true);
        ss.setDeactivatedAt(null);

        StaffStation saved = staffStationRepository.save(ss);
        return staffStationMapper.toStaffStationDto(saved);
    }

    @Override
    public StaffStationDto deactivateAssignment(Long id) {
        userValidation.validateAdmin();

        if (ObjectUtils.isEmpty(id)) {
            throw new InvalidateParamsException("id không được để trống");
        }

        StaffStation assignment = staffStationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phân công với id = " + id));

        if (!Boolean.TRUE.equals(assignment.getIsActive())) {
            throw new InvalidateParamsException("Phân công đã bị vô hiệu");
        }

        assignment.setIsActive(false);
        assignment.setDeactivatedAt(LocalDateTime.now());

        StaffStation saved = staffStationRepository.save(assignment);
        return staffStationMapper.toStaffStationDto(saved);
    }

    @Override
    public List<StaffStationDto> listAssignments(Long stationId) {
        userValidation.validateAdmin();

        List<StaffStation> list;
        if (stationId == null) {
            list = staffStationRepository.findAll();
        } else {
            list = staffStationRepository.findAllByStationId(stationId);
        }

        return list.stream().map(staffStationMapper::toStaffStationDto).collect(Collectors.toList());
    }
}
