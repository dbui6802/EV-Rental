package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.DepositStatusJpaConverter;
import com.webserver.evrentalsystem.jpaconverter.RentalStatusJpaConverter;
import com.webserver.evrentalsystem.jpaconverter.RentalTypeJpaConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "station_pickup_id", nullable = false)
    private Station stationPickup;

    @ManyToOne
    @JoinColumn(name = "station_return_id")
    private Station stationReturn;

    @ManyToOne
    @JoinColumn(name = "staff_pickup_id", nullable = false)
    private User staffPickup;

    @ManyToOne
    @JoinColumn(name = "staff_return_id")
    private User staffReturn;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "total_distance")
    private Double totalDistance;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Convert(converter = RentalTypeJpaConverter.class)
    @Column(name = "rental_type", nullable = false)
    private RentalType rentalType; // booking, walk-in

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Convert(converter = DepositStatusJpaConverter.class)
    @Column(name = "deposit_status", nullable = false)
    private DepositStatus depositStatus; // pending, held, refunded

    @Convert(converter = RentalStatusJpaConverter.class)
    @Column(nullable = false)
    private RentalStatus status; // booked, in_use, returned, waiting_for_payment, cancelled

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
