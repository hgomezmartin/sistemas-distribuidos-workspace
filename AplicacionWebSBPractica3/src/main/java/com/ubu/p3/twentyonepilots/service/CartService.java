package com.ubu.p3.twentyonepilots.service;

import com.ubu.p3.twentyonepilots.model.OrderItem;
import com.ubu.p3.twentyonepilots.model.Product;
import com.ubu.p3.twentyonepilots.model.Ticket;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@SessionScope          // carrito por sesión de usuario
public class CartService {

    /** key = "P:ID"  o  "T:ID" */
    private final Map<String, OrderItem> items = new LinkedHashMap<>();

    //añadir
    public void addProduct(Product p) {
        String k = "P:" + p.getId();
        items.compute(k, (key, it) -> {
            if (it == null) {
                it = new OrderItem();
                it.setProduct(p);
                it.setQuantity(0);
            }
            it.setQuantity(it.getQuantity() + 1);
            return it;
        });
    }

    public void addTicket(Ticket t) {
        String k = "T:" + t.getId();
        items.compute(k, (key, it) -> {
            if (it == null) {
                it = new OrderItem();
                it.setTicket(t);
                it.setQuantity(0);
            }
            it.setQuantity(it.getQuantity() + 1);
            return it;
        });
    }

    //getters
    public Collection<OrderItem> list() { return items.values(); }

    public BigDecimal total() {
        return items.values().stream()
                .map(it -> {
                    BigDecimal price = (it.getProduct() != null)
                            ? it.getProduct().getPrice()
                            : it.getTicket().getPrice();
                    return price.multiply(BigDecimal.valueOf(it.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // limpieza
    public void clear() { items.clear(); }
}