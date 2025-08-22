package com.order.product.service.order;

import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.OrderForm;
import com.order.product.model.dto.OrderResponse;
import com.order.product.model.dto.TotalQuantityProductResponse;
import com.order.product.model.entity.OrderUser;

import java.util.List;

public interface IOrderService {
    CartResponse createNewOrder(OrderForm orderForm);

    List<OrderResponse> getAllOrder();
    List<OrderResponse> getAllOrderOfUser(int userId);

    OrderUser processOrder(int orderId);
    OrderUser doneOrder(int orderId);
    List<TotalQuantityProductResponse> findListTotalProductOfMonth(int month);

    List<TotalQuantityProductResponse> getAllTotalPriceOfYear();


}
