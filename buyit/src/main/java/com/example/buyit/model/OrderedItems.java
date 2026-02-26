package com.example.buyit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderedItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "prod_id", nullable = false)
    private Product product;

    private Long quantity;
    private Long price;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    public OrderedItems(Product product, Long quantity, Orders order) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice() * quantity;
        this.order = order;
        this.appUser = order.getAppUser();
    }
}
