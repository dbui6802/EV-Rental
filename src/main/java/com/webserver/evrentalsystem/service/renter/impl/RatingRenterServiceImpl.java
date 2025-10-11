package com.webserver.evrentalsystem.service.renter.impl;

import com.webserver.evrentalsystem.entity.Rating;
import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.StaffRating;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.RatingDto;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffRatingDto;
import com.webserver.evrentalsystem.model.dto.request.RatingRequest;
import com.webserver.evrentalsystem.model.mapping.RatingMapper;
import com.webserver.evrentalsystem.model.mapping.StaffRatingMapper;
import com.webserver.evrentalsystem.repository.RatingRepository;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.repository.StaffRatingRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.renter.RatingRenterService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class RatingRenterServiceImpl implements RatingRenterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private StaffRatingRepository staffRatingRepository;

    @Autowired
    private StaffRatingMapper staffRatingMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public RatingDto rateTrip(RatingRequest request) {
        User renter = userValidation.validateRenter();
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến đi."));

        Rating rating = new Rating();
        rating.setRental(rental);
        rating.setRenter(renter);
        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        rating.setCreatedAt(LocalDateTime.now());

        return ratingMapper.toRatingDto(ratingRepository.save(rating));
    }

    @Override
    public StaffRatingDto rateStaff(RatingRequest request) {
        User renter = userValidation.validateRenter();
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến đi."));

        var staff = userRepository.findById(request.getStaffId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên."));

        StaffRating rating = new StaffRating();
        rating.setRental(rental);
        rating.setRenter(renter);
        rating.setStaff(staff);
        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        rating.setCreatedAt(LocalDateTime.now());

        return staffRatingMapper.toStaffRatingDto(staffRatingRepository.save(rating));
    }
}
