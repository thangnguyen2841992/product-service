package com.order.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderForm {
    private int userId;
    private int cartId;

    private String description;

    private int deliveryId;

    private int paymentId;

    private double totalPrice;
}
