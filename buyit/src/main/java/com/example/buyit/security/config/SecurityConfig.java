package com.example.buyit.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.buyit.service.AdminService;
import com.example.buyit.service.RegistrationService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RegistrationService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;

    public SecurityConfig(RegistrationService appUserService, PasswordEncoder passwordEncoder, AdminService adminService) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
        this.adminService = adminService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**", "/users/api/**", "/users/register/**", "/api/v1/user/**", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .defaultSuccessUrl("/users/home", true)
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(
                appUserService); // Points to your custom service
        authProvider.setPasswordEncoder(passwordEncoder); // Points to your password encoder
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
