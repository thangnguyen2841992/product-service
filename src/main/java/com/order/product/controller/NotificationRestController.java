package com.order.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.MessageError;
import com.order.product.model.dto.OrderForm;
import com.order.product.service.notification.INotificationService;
import com.order.product.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationRestController {
    @Autowired
    private INotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

}
