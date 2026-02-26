package com.example.buyit.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.buyit.model.AppUser;
import com.example.buyit.model.OrderedItems;
import com.example.buyit.model.ProductRankings;

public interface OrderedItemsRepository extends JpaRepository<OrderedItems, Long> {

    List<OrderedItems> findAllByAppUser(AppUser user);

    List<OrderedItems> findAllByOrder_OrderId(String orderId);

    @Query("SELECT new com.example.buyit.model.ProductRankings(oi.product, SUM(oi.quantity))  FROM OrderedItems oi GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
    List<ProductRankings> findTopSellers(Pageable pageable);

    @Query("SELECT new com.example.buyit.model.ProductRankings(p, SUM(oi.quantity)) FROM OrderedItems oi JOIN oi.product p WHERE p.category = :category GROUP BY p ORDER BY SUM(oi.quantity) DESC ")
    List<ProductRankings> findTopSellersByCategory(
            @Param("category") String category,
            Pageable pageable
    );

    @Query("SELECT new com.example.buyit.model.ProductRankings(p, SUM(oi.quantity)) FROM OrderedItems oi JOIN oi.product p WHERE p.category = :category GROUP BY p ORDER BY SUM(oi.quantity) ASC ")
    List<ProductRankings> findBottomSellersByCategory(
            @Param("category") String category,
            Pageable pageable
    );

    @Query("SELECT new com.example.buyit.model.ProductRankings(oi.product, SUM(oi.quantity)) FROM OrderedItems oi GROUP BY oi.product ORDER BY SUM(oi.quantity) ASC")
    List<ProductRankings> findBottomSellers(Pageable pageable);
}
