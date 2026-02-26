package com.example.buyit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buyit.model.Orders;
import com.example.buyit.model.AppUser;

public interface OrderRepository extends JpaRepository<Orders, String> {

    Optional<Orders> findByOrderId(String orderId);

    Optional<Orders> findByOrderIdAndAppUser(String orderId, AppUser user);

    List<Orders> findAllByAppUser(AppUser user);
}
