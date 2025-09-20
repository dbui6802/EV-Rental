package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.CheckTypeJpaConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rental_checks")
public class RentalCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    @Convert(converter = CheckTypeJpaConverter.class)
    @Column(name = "check_type", nullable = false)
    private CheckType checkType; // pickup, return

    @Column(name = "condition_report", columnDefinition = "TEXT")
    private String conditionReport;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "customer_signature_url")
    private String customerSignatureUrl;

    @Column(name = "staff_signature_url")
    private String staffSignatureUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
