package com.webserver.evrentalsystem.specification;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.entity.ReservationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ReservationSpecification {

    public static Specification<Reservation> hasRenter(Long renterId) {
        return (root, query, cb) ->
                renterId == null ? null : cb.equal(root.get("renter").get("id"), renterId);
    }

    public static Specification<Reservation> hasVehicle(Long vehicleId) {
        return (root, query, cb) ->
                vehicleId == null ? null : cb.equal(root.get("vehicle").get("id"), vehicleId);
    }

    public static Specification<Reservation> hasStatus(ReservationStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Reservation> startFrom(LocalDateTime startFrom) {
        return (root, query, cb) ->
                startFrom == null ? null : cb.greaterThanOrEqualTo(root.get("reservedStartTime"), startFrom);
    }

    public static Specification<Reservation> startTo(LocalDateTime startTo) {
        return (root, query, cb) ->
                startTo == null ? null : cb.lessThanOrEqualTo(root.get("reservedStartTime"), startTo);
    }
}
