package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.ReservationStatusJpaConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "reserved_start_time", nullable = false)
    private LocalDateTime reservedStartTime;

    @Column(name = "reserved_end_time", nullable = false)
    private LocalDateTime reservedEndTime;

    @ManyToOne
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "cancelled_reason")
    private String cancelledReason;

    @Convert(converter = ReservationStatusJpaConverter.class)
    @Column(nullable = false)
    private ReservationStatus status; // pending, confirmed, cancelled, expired

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}