package com.order.product.service.order;

import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.OrderForm;
import com.order.product.model.dto.OrderResponse;

import java.util.List;

public interface IOrderService {
    CartResponse createNewOrder(OrderForm orderForm);

    List<OrderResponse> getAllOrder();
}
