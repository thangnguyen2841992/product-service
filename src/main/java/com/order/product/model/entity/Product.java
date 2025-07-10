package com.order.product.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false)
    private String productName;


    private double productPrice;

    private String description;

    private Date dateCreated;
    private Date dateUpdated;

    private int point;
    private int quantity;
    private boolean isDelete;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    private ProductUnit productUnit;
}
