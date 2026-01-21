package com.example.buyit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.buyit.model.AppUser;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByPhoneno(String phoneno);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.isenabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    Optional<AppUser> findByToken(String token);
}
