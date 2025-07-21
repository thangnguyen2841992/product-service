package com.order.product.service.order;

import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.OrderForm;

public interface IOrderService {
    CartResponse createNewOrder(OrderForm orderForm);
}
