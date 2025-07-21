package com.order.product.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    private int userId;
    private double totalPrice;
    private boolean isDelete;
    private boolean isProcess;
    private boolean isPaymented;
    private boolean isDone;
    private int deliveryId;
    private int paymentId;
    private String description;
    private Date dateCreated;
    private Date dateUpdated;

}
