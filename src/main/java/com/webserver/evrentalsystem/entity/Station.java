package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.StationStatusJpaConverter;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Convert(converter = StationStatusJpaConverter.class)
    @Column(nullable = false)
    private StationStatus status;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}