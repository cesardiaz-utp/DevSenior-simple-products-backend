package com.devsenior.simple_products_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsenior.simple_products_backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
