package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.PaymentMethodJpaConverter;
import com.webserver.evrentalsystem.jpaconverter.PaymentStatusJpaConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(nullable = false)
    private BigDecimal amount;

    @Convert(converter = PaymentMethodJpaConverter.class)
    @Column(nullable = false)
    private PaymentMethod method; // cash, card, e-wallet

    @Convert(converter = PaymentStatusJpaConverter.class)
    @Column(nullable = false)
    private PaymentStatus status; // pending, paid, failed

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
