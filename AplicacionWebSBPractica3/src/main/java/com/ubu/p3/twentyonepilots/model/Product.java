package com.ubu.p3.twentyonepilots.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * el merchandising oficial (camiseta, sudadera, cd…).
 */

@Entity
@Getter @Setter @NoArgsConstructor
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //nombre
    private String description; // su respectiva descripcion

    @Column(precision = 10, scale = 2) //admite muchos euros para futuras actualizaciones jajaj
    private BigDecimal price;

    private String imageUrl; // ruta relativa dentro de /static/images
}
