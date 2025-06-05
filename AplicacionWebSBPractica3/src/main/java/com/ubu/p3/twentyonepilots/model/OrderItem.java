package com.ubu.p3.twentyonepilots.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="order_id") //revisar
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product; // uno u otro será null

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;
}
