package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.IncidentStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "incident_reports")
public class IncidentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = true)
    private User staff;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Convert(converter = IncidentStatusConverter.class)
    @Column(nullable = false)
    private IncidentStatus status;

    @Column(columnDefinition = "TEXT", name = "resolution_notes")
    private String resolutionNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
