package com.example.buyit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductReg {

    private String prod_name;
    private String image_Link;
    private String category;
    private Long quantity;
    private Long price;
}
