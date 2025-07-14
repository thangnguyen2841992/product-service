package com.order.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.product.model.dto.CartForm;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.entity.ProductCart;
import com.order.product.service.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartRestController {
    @Autowired
    private ICartService cartService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/add-cart")
    public void addNewCart(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CartForm cartForm = objectMapper.readValue(message, CartForm.class);
            CartResponse cartResponse = this.cartService.saveNewCart(cartForm);
            simpMessagingTemplate.convertAndSend("/topic/cart", cartResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllProductCartOfUserId")
    public ResponseEntity<List<ProductCart>> getAllProductCartOfUserId(@RequestParam(name = "userId") int userId) {
        return new ResponseEntity<>(this.cartService.findProductCartByUserId(userId), HttpStatus.OK);
    }
}
