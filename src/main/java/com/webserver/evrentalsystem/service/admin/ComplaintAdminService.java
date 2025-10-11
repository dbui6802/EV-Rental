package com.webserver.evrentalsystem.service.admin;


import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import com.webserver.evrentalsystem.model.dto.request.ResolveComplaintRequest;

import java.util.List;

public interface ComplaintAdminService {
    List<ComplaintDto> getAllComplaints(String status);
    ComplaintDto resolveComplaint(ResolveComplaintRequest request);
}
