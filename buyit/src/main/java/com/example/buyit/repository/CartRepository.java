package com.example.buyit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.buyit.model.AppUser;
import com.example.buyit.model.Cart;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByProduct_ProdIdAndAppUser(Long productId, AppUser user);

    List<Cart> findAllByAppUser(AppUser user);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.quantity = c.quantity + 1 WHERE c.product.prodId = ?1 AND c.appUser.username = ?2")
    int incrementQuantity(Long productId, String username);

    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM Cart c WHERE c.appUser.username = ?1")
    Long getTotalCartCount(String username);

}
