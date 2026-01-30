package com.example.buyit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buyit.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByOrderId(String orderId);

    Optional<Orders> findByOrderIdAndAppUser(String orderId, com.example.buyit.model.AppUser user);

    List<Orders> findAllByAppUser(com.example.buyit.model.AppUser user);
}
