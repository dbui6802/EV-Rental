package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.RentalStatus;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.mapping.RentalMapper;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.service.admin.RentalAdminService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RentalAdminServiceImpl implements RentalAdminService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private UserValidation userValidation;

    public List<RentalDto> getRentals(Long renterId, Long vehicleId, Long stationPickupId,
                                      Long stationReturnId, String status,
                                      LocalDateTime startFrom, LocalDateTime startTo) {
        userValidation.validateAdmin();

        Specification<Rental> spec = Specification.where(null);

        if (renterId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("renter").get("id"), renterId));
        }
        if (vehicleId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("vehicle").get("id"), vehicleId));
        }
        if (stationPickupId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("stationPickup").get("id"), stationPickupId));
        }
        if (stationReturnId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("stationReturn").get("id"), stationReturnId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), RentalStatus.valueOf(status.toUpperCase())));
        }
        if (startFrom != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startTime"), startFrom));
        }
        if (startTo != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("startTime"), startTo));
        }

        return rentalRepository.findAll(spec).stream()
                .map(rentalMapper::toRentalDto)
                .toList();
    }
}
