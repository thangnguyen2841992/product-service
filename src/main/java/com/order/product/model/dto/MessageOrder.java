package com.order.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageOrder {
    private int userId;
    private String fullName;
    private  int gender;
    private int totalProduct;
    private double totalPrice;
    private String toEmail;
    private int orderId;

}
