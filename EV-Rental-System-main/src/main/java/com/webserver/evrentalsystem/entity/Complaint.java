package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.ComplaintStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "complaints")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff; // nhân viên bị khiếu nại

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin; // admin xử lý

    @Column(columnDefinition = "TEXT")
    private String description;

    @Convert(converter = ComplaintStatusConverter.class)
    @Column(nullable = false)
    private ComplaintStatus status;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
