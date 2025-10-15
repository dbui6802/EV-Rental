package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.model.dto.response.StaffPerformanceResponse;
import com.webserver.evrentalsystem.repository.StaffPerformanceRepository;
import com.webserver.evrentalsystem.service.admin.PerformanceAdminService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceAdminServiceImpl implements PerformanceAdminService {

    @Autowired
    private StaffPerformanceRepository staffPerformanceRepository;

    @Autowired
    private UserValidation userValidation;

    @Override
    public List<StaffPerformanceResponse> getAllPerformance() {
        userValidation.validateAdmin();
        return staffPerformanceRepository.getAllStaffPerformance();
    }
}
