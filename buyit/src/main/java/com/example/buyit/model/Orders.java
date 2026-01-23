package com.example.buyit.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String paymentId;
    private LocalDate orderDate;
    private LocalDate deliveryDate;

    private Long totalPrice;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;
    @OneToMany(mappedBy = "order")
    private List<OrderedItems> items;

    public Orders(String orderId, Long totalPrice, AppUser appUser) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.appUser = appUser;
        this.orderDate = LocalDate.now();
        this.deliveryDate = LocalDate.now().plusDays(2);

    }
}
