package com.ubu.p3.twentyonepilots.repository;

import com.ubu.p3.twentyonepilots.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
