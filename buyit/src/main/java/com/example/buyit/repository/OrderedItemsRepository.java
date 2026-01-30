package com.example.buyit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buyit.model.OrderedItems;

public interface OrderedItemsRepository extends JpaRepository<OrderedItems, Long> {

    List<OrderedItems> findAllByAppUser(com.example.buyit.model.AppUser user);

    List<OrderedItems> findAllByOrderId(Long orderId);

}
