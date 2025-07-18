package com.order.product.service.order;

import com.order.product.repository.IOrderRepository;
import com.order.product.repository.IProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService{
    @Autowired
    private IProductOrderRepository productOrderRepository;

    @Autowired
    private IOrderRepository orderRepository;
}
