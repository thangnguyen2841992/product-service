package com.order.product.model.dto;

public interface TotalQuantityProductResponse {
    long getProductId();
    String getProductName();
    long getTotalQuantity();
    double getTotalPrice();
    int getMonth();
    double getProductPrice();
    String getBrandName();
    String getProductUnitName();
}
