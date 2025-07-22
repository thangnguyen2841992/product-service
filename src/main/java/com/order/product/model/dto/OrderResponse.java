package com.order.product.model.dto;

import com.order.product.model.entity.Delivery;
import com.order.product.model.entity.Payment;
import com.order.product.model.entity.ProductOrder;
import com.order.product.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
    private int orderId;
    private User userId;
    private double totalPrice;
    private boolean isDelete;
    private boolean isProcess;
    private boolean isPaymented;
    private boolean isDone;
    private Delivery deliveryId;
    private Payment paymentId;
    private String description;
    private Date dateCreated;
    private Date dateUpdated;
    private List<ProductOrder> productOrder;
}
