package com.webserver.evrentalsystem.service.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.RatingDto;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffRatingDto;
import com.webserver.evrentalsystem.model.dto.request.RatingRequest;

public interface RatingRenterService {
    RatingDto rateTrip(RatingRequest request);
    StaffRatingDto rateStaff(RatingRequest request);
}
