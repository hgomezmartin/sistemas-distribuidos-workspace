package com.ubu.p3.twentyonepilots.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * linea individual de un pedido q puede ser un proucto del merchandise o un tickett pero nunca ambos a la vez
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity = 1; //unidades compradas

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY) //producto
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) //entrada
    private Ticket ticket;

    //MÉTODOS CALCULADOS

    //Nombre que mostrará el carrito
    public String getName() {
        return (product != null)
                ? product.getName() // ej. hoodie bandito
                : ticket.getCity() + " · " + ticket.getDate(); // ej. Madrid - fecha
    }

    //Subtotal = precio * cantidad
    public BigDecimal getSubtotal() {
        BigDecimal price = (product != null)
                ? product.getPrice()
                : ticket.getPrice();
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
