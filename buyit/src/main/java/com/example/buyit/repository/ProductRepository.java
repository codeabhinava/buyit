package com.example.buyit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import com.example.buyit.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory(String category);

    List<Product> findByCategory(String category);

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByQuantityAsc();

    @Query("Select p.category from Product p group by p.category")
    List<String> findDistinctCategories();

}
