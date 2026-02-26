package com.example.buyit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "prod_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodId;

    @Column(name = "prod_name")
    private String name;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "category")
    private String category;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private Long price;

    private boolean show;

    public Product(String name, String imageLink, String category, long quantity, long price) {
        this.name = name;
        this.imageLink = imageLink;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
}
