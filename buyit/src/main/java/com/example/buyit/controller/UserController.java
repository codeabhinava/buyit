package com.example.buyit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.buyit.model.AppReg;
import com.example.buyit.model.AppUser;
import com.example.buyit.service.RegistrationService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/register")
    public String registerUser(@RequestBody AppReg appreg) {
        return registrationService.register(appreg);
    }

    @GetMapping("/register/confirm")
    public String confirmUser(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        return registrationService.deleteUser(id);
    }

    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody AppReg appreg) {
        return registrationService.updateUser(id, appreg);
    }

    @GetMapping("/all")
    public List<AppUser> allUsers() {
        return registrationService.getAllUsers();
    }

    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return registrationService.getById(id);
    }

    @GetMapping("/token/{token}")
    public AppUser getUserByToken(@PathVariable String token) {
        return registrationService.getByToken(token);
    }
}
