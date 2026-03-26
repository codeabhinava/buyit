package com.example.buyit.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Admin implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String adminNo;

    @Column(nullable = false)
    private String password;

    private int unsolved_tickets;

    private int solved_tickets;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean isEnabled;

    public Admin(String adminNo, String password, int unsolved_tickets, int solved_tickets, UserRole userRole, Boolean isEnabled) {
        this.adminNo = adminNo;
        this.password = password;
        this.unsolved_tickets = unsolved_tickets;
        this.solved_tickets = solved_tickets;
        this.userRole = UserRole.ADMIN;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return adminNo;
    }

}
