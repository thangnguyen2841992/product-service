package com.order.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.product.model.dto.CartForm;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.ChatApproval;
import com.order.product.model.entity.Chat;
import com.order.product.service.chat.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRestController {
    @Autowired
    private IChatService chatService;

    @MessageMapping("/add-chat")
    public void addNewChat(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Chat newChat = objectMapper.readValue(message, Chat.class);
            this.chatService.addChat(newChat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/approval-chat")
    public void approvalChat(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatApproval newChat = objectMapper.readValue(message, ChatApproval.class);
            this.chatService.approvalChat(newChat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("chat/getAllChat")
    public ResponseEntity<List<Chat>> getAllChat(@RequestParam("chatId") int chatId) {
        List<Chat> chats = this.chatService.findAllChatOfUser(chatId);
        return ResponseEntity.ok(chats);
    }
}
