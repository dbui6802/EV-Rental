package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.ErrorStatusJpaConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "error")
public class Error {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Convert(converter = ErrorStatusJpaConverter.class)
    @Column(name = "status")
    private ErrorStatus status;

    @Column(name = "lastTimeOccurred")
    private Long lastTimeOccurred;

    @Column(name = "lastTimeClosed")
    private Long lastTimeClosed;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
