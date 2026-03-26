package com.example.buyit.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.buyit.model.Admin;
import com.example.buyit.model.AdminRegistration;
import com.example.buyit.model.AppUser;
import com.example.buyit.model.ProductRankings;
import com.example.buyit.model.UserRole;
import com.example.buyit.repository.AdminRepository;
import com.example.buyit.repository.AppUserRepository;
import com.example.buyit.repository.OrderedItemsRepository;
import com.example.buyit.security.PasswordEncoderConfig;

@Service
public class AdminService {

    private final AdminRepository adminRepo;
    private final AppUserRepository appUserRepo;

    private final OrderedItemsRepository orderedItemsRepo;
    private final PasswordEncoderConfig passwordEncoder;

    public AdminService(AdminRepository adminRepo, AppUserRepository appUserRepo, PasswordEncoderConfig passwordEncoder, OrderedItemsRepository orderedItemsRepo) {

        this.appUserRepo = appUserRepo;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.orderedItemsRepo = orderedItemsRepo;
    }

    public void add(AdminRegistration adminRegistration) {
        String encoded = passwordEncoder.bCryptPasswordEncoder().encode(adminRegistration.getPassword());
        Admin newadmin = new Admin(adminRegistration.getAdmin_no(), encoded, 0, 0, UserRole.ADMIN, true);

        AppUser adminUser = new AppUser(adminRegistration.getAdmin_no(), null, null, encoded, UserRole.ADMIN, null);
        appUserRepo.save(adminUser);
        appUserRepo.enableAllAdmins();
        adminRepo.save(newadmin);
    }

    public List<ProductRankings> topSeller() {
        return orderedItemsRepo.findTopSellers(PageRequest.of(0, 5));

    }

    public List<ProductRankings> topSellerCategory(String category) {
        return orderedItemsRepo.findTopSellersByCategory(category, PageRequest.of(0, 5));
    }

    public List<ProductRankings> bottomSellerCategory(String category) {
        return orderedItemsRepo.findBottomSellersByCategory(category, PageRequest.of(0, 5));
    }

    public List<ProductRankings> bottomSeller() {
        return orderedItemsRepo.findBottomSellers(PageRequest.of(0, 5));

    }

}
