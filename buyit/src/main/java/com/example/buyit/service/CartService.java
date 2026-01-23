package com.example.buyit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.buyit.model.AppUser;
import com.example.buyit.model.Cart;
import com.example.buyit.model.Product;
import com.example.buyit.repository.AppUserRepository;
import com.example.buyit.repository.CartRepository;
import com.example.buyit.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ProductRepository productRepository;

    public String addProductToCart(String username, Long prodId) {
        AppUser user = appUserRepository.findByUsername(username).orElseThrow();

        // Match the new method name here
        if (cartRepository.findByProduct_ProdIdAndAppUser(prodId, user).isPresent()) {
            cartRepository.incrementQuantity(prodId, username);
            return "Product quantity updated in cart";
        }

        Cart cart = new Cart(productRepository.findById(prodId).orElseThrow(), 1L, user);
        cartRepository.save(cart);
        return "Product added to cart";
    }

    public int viewCart(String username) {

        AppUser user = appUserRepository.findByUsername(username).orElseThrow();
        List<Cart> cart = cartRepository.findAllByAppUser(user);
        int total = 0;
        for (Cart c : cart) {
            Product p = c.getProduct();
            total += c.getQuantity() * p.getPrice();
        }

        return total;
    }
}
