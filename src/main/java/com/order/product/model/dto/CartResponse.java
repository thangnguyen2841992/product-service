package com.order.product.model.dto;

import com.order.product.model.entity.ProductCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResponse {
    private int cartId;

    private int userId;

    List<ProductCart> productCartList;

    private Date dateCreated;

    private long totalPrice;

    private long totalProduct;
}
