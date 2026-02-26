package com.example.buyit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.buyit.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory(String category);

    List<Product> findByCategory(String category);

    List<Product> findAllByOrderByProdIdAsc();

    @Query("Select p from Product p where p.show = TRUE")
    Page<Product> findAllProducts(Pageable pageable);

    @Query("Select p from Product p where p.show = TRUE ORDER BY p.price ASC")
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);

    @Query("Select p from Product p where p.show = TRUE ORDER BY p.quantity ASC")
    Page<Product> findAllByOrderByQuantity(Pageable pageable);

    @Query("Select p.category from Product p where p.show = TRUE group by p.category")
    List<String> findDistinctCategories();

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.show = FALSE WHERE p.prodId = ?1")
    int hideProduct(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.show = TRUE WHERE p.prodId = ?1")
    int showProduct(Long id);

}
