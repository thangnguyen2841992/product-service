package com.order.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.product.model.dto.CartForm;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.MessageError;
import com.order.product.model.dto.OrderForm;
import com.order.product.service.cart.ICartService;
import com.order.product.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/add-order")
    public void addOrder(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            OrderForm orderForm = objectMapper.readValue(message, OrderForm.class);
            CartResponse cartResponse = this.orderService.createNewOrder(orderForm);
            simpMessagingTemplate.convertAndSend("/topic/cart", cartResponse);
            MessageError messageError = new MessageError();
            messageError.setMessage("Bạn đã đặt hàng thành công. Chúng tôi đang xử lý đơn hàng của bạn.");
            simpMessagingTemplate.convertAndSend("/topic/order", messageError);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
