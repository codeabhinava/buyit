package com.example.buyit.model;

import lombok.Data;

@Data
public class ProductRankings {

    private Product products;
    private Long quantity;

    public ProductRankings(Product products, Long quantity) {
        this.products = products;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return products;
    }

    public Long getQuantity() {
        return quantity;
    }
}
