package com.webserver.evrentalsystem.specification;

import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.RentalStatus;
import com.webserver.evrentalsystem.entity.Station;
import com.webserver.evrentalsystem.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RentalSpecification {

    public static Specification<Rental> hasRenter(Long renterId) {
        return (root, query, cb) -> renterId == null ? cb.conjunction() : cb.equal(root.get("renter").get("id"), renterId);
    }

    public static Specification<Rental> hasVehicle(Long vehicleId) {
        return (root, query, cb) -> vehicleId == null ? cb.conjunction() : cb.equal(root.get("vehicle").get("id"), vehicleId);
    }

    public static Specification<Rental> hasStationPickup(Long stationPickupId) {
        return (root, query, cb) -> stationPickupId == null ? cb.conjunction() : cb.equal(root.get("stationPickup").get("id"), stationPickupId);
    }

    public static Specification<Rental> hasStationReturn(Long stationReturnId) {
        return (root, query, cb) -> stationReturnId == null ? cb.conjunction() : cb.equal(root.get("stationReturn").get("id"), stationReturnId);
    }

    public static Specification<Rental> hasStatus(RentalStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Rental> startFrom(LocalDateTime startFrom) {
        return (root, query, cb) -> startFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("startTime"), startFrom);
    }

    public static Specification<Rental> startTo(LocalDateTime startTo) {
        return (root, query, cb) -> startTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("startTime"), startTo);
    }

    public static Specification<Rental> isAtStaffStation(Station staffStation) {
        return (root, query, cb) -> {
            if (staffStation == null) {
                return cb.disjunction();
            }
            Predicate pickupStationMatch = cb.equal(root.get("stationPickup").get("id"), staffStation.getId());
            Predicate returnStationMatch = cb.equal(root.get("stationReturn").get("id"), staffStation.getId());
            return cb.or(pickupStationMatch, returnStationMatch);
        };
    }

    public static Specification<Rental> isHandledBy(User staff) {
        return (root, query, cb) -> {
            if (staff == null) {
                return cb.disjunction();
            }
            Predicate pickupStaffMatch = cb.equal(root.get("staffPickup").get("id"), staff.getId());
            Predicate returnStaffMatch = cb.equal(root.get("staffReturn").get("id"), staff.getId());
            return cb.or(pickupStaffMatch, returnStaffMatch);
        };
    }
}
