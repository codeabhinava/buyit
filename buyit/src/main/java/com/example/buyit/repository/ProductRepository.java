package com.example.buyit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buyit.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory(String category);

}
