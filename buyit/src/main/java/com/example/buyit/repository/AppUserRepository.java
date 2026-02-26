package com.example.buyit.repository;

import java.util.List;
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

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.isenabled = FALSE WHERE a.email = ?1")
    int disableAppUser(String email);

    @Query("Select a from AppUser a where a.userRole = 'USER' order by a.id ASC")
    List<AppUser> findallusers();

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.isenabled = TRUE WHERE a.userRole = 'ADMIN'")
    int enableAllAdmins();

    Optional<AppUser> findByToken(String token);

}
