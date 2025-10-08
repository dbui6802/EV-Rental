package com.webserver.evrentalsystem.specification;

import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.entity.VehicleStatus;
import com.webserver.evrentalsystem.entity.VehicleType;
import org.springframework.data.jpa.domain.Specification;

public class VehicleSpecification {
    public static Specification<Vehicle> hasType(VehicleType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Vehicle> hasStation(Long stationId) {
        return (root, query, cb) ->
                stationId == null ? null : cb.equal(root.get("station").get("id"), stationId);
    }

    public static Specification<Vehicle> priceGreaterOrEqual(Double priceMin) {
        return (root, query, cb) ->
                priceMin == null ? null : cb.greaterThanOrEqualTo(root.get("pricePerHour"), priceMin);
    }

    public static Specification<Vehicle> priceLessOrEqual(Double priceMax) {
        return (root, query, cb) ->
                priceMax == null ? null : cb.lessThanOrEqualTo(root.get("pricePerHour"), priceMax);
    }

    public static Specification<Vehicle> isAvailable() {
        return (root, query, cb) -> cb.equal(root.get("status"), VehicleStatus.AVAILABLE);
    }
}

