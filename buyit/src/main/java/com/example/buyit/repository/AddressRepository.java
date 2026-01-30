package com.example.buyit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buyit.model.Address;
import com.example.buyit.model.AppUser;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByAppUser(AppUser appUser);

    Optional<Address> findByAppUser(AppUser appUser);
}
