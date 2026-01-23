package com.example.buyit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.buyit.model.Product;
import com.example.buyit.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getByCategory(String category) {
        return productRepository.findAllByCategory(category);
    }

}
