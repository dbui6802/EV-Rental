package com.webserver.evrentalsystem.service.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import com.webserver.evrentalsystem.model.dto.request.ComplaintRequest;

public interface ComplaintRenterService {
    ComplaintDto createComplaint(ComplaintRequest request);
}
