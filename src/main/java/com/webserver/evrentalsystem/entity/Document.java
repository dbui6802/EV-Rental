package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.DocumentTypeJpaConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Convert(converter = DocumentTypeJpaConverter.class)
    @Column(nullable = false)
    private DocumentType type;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "document_url")
    private String documentUrl;

    @Column
    private Boolean verified;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
