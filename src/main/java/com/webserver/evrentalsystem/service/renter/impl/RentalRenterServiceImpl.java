package com.webserver.evrentalsystem.service.renter.impl;

import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.RentalStatus;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.mapping.RentalMapper;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.service.renter.RentalRenterService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class RentalRenterServiceImpl implements RentalRenterService {

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Override
    public List<RentalDto> getAllRentalsOfRenter(String status, String fromDate, String toDate) {
        User renter = userValidation.validateRenter();

        List<Rental> rentals = rentalRepository.findByRenterId(renter.getId());

        // filter theo status
        if (status != null) {
            rentals = rentals.stream()
                    .filter(r -> Objects.equals(r.getStatus().name().toLowerCase(), status.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // filter theo khoảng ngày
        if (fromDate != null) {
            LocalDateTime from = LocalDate.parse(fromDate).atStartOfDay();
            rentals = rentals.stream()
                    .filter(r -> r.getStartTime().isAfter(from))
                    .collect(Collectors.toList());
        }
        if (toDate != null) {
            LocalDateTime to = LocalDate.parse(toDate).atTime(23, 59, 59);
            rentals = rentals.stream()
                    .filter(r -> r.getStartTime().isBefore(to))
                    .collect(Collectors.toList());
        }

        return rentals.stream().map(rentalMapper::toRentalDto).collect(Collectors.toList());
    }
}
