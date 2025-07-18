package com.order.product.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue
    private int productOrderId;

    private int orderId;

    private int quantity;

    private Date dateCreated;

    private boolean isDelete;
}
