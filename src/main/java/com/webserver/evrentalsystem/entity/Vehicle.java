package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.VehicleStatusJpaConverter;
import com.webserver.evrentalsystem.jpaconverter.VehicleTypeJpaConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Convert(converter = VehicleTypeJpaConverter.class)
    @Column(nullable = false)
    private VehicleType type; // motorbike, car

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "range_per_full_charge", nullable = false)
    private Integer rangePerFullCharge;

    @Convert(converter = VehicleStatusJpaConverter.class)
    @Column(nullable = false)
    private VehicleStatus status; // reserved, available, rented, maintenance

    @Column(name = "price_per_hour")
    private BigDecimal pricePerHour;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;
}
