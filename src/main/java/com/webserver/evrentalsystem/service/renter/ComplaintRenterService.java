package com.webserver.evrentalsystem.service.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import com.webserver.evrentalsystem.model.dto.request.ComplaintRequest;

import java.util.List;

public interface ComplaintRenterService {
    ComplaintDto createComplaint(ComplaintRequest request);

    List<ComplaintDto> getComplaintsOfRenter();

    ComplaintDto getComplaintDetail(Long complaintId);
}
