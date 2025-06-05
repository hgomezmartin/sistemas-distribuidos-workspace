package com.ubu.p3.twentyonepilots.service;

import com.ubu.p3.twentyonepilots.model.Product;
import com.ubu.p3.twentyonepilots.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * gestion de producots
 */

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public List<Product> list() {
        return repo.findAll();
    }

    public Product get(Long id)  {
        return repo.findById(id).orElseThrow();
    }

    public Product save(Product p) {
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}