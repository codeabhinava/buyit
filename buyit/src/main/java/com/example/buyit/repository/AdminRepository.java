package com.example.buyit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buyit.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminNo(String admin_no);

}
