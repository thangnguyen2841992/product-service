package com.order.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageProcessOrder {
    private String toEmail;
    private String message;
    private int orderId;
    private String fullName;
}
