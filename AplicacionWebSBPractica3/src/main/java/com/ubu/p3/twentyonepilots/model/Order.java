package com.ubu.p3.twentyonepilots.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Pedido realizado por un usuario, el usuario hace 1 N pedidos ycada pedido contiene N líneas de tipo OrderItem
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id incrementa

    private LocalDateTime createdAt = LocalDateTime.now(); //fecha de compra

    @ManyToOne(optional = false) //un pedido pertenece a un solo usuarop
    private User user;// quién hizo el pedido

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
