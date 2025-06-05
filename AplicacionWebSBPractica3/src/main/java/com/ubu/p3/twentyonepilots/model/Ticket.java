package com.ubu.p3.twentyonepilots.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 3ntrada para uno de los conciertos
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private LocalDate date;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
}
