package com.ubu.p3.twentyonepilots.repository;

import com.ubu.p3.twentyonepilots.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
