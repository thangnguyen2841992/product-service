package com.order.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.product.model.dto.ChatRequest;
import com.order.product.model.dto.ChatResponse;
import com.order.product.model.dto.ChatRoomResponse;
import com.order.product.model.dto.WaitingChatResponse;
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
            ChatRequest newChat = objectMapper.readValue(message, ChatRequest.class);
            this.chatService.addChat(newChat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @MessageMapping("/add-chatRoom")
    public void addChatRoom(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatRequest newChat = objectMapper.readValue(message, ChatRequest.class);
            this.chatService.addChatRoom(newChat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @MessageMapping("/close-chatRoom")
    public void closeChatRoom(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatRequest newChat = objectMapper.readValue(message, ChatRequest.class);
            this.chatService.closeChatRoom(newChat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/staff-api/findWaitingChatByDeleted")
    public ResponseEntity<List<WaitingChatResponse>> findWaitingChatByDeleted() {
        return ResponseEntity.ok(this.chatService.findWaitingChatByDeleted());
    }

    @GetMapping("/staff-api/getAllChatRoomOfStaffId")
    public ResponseEntity<List<ChatRoomResponse>> getAllChatRoomOfStaffId(@RequestParam("staffId") int staffId) {
        return ResponseEntity.ok(this.chatService.getAllChatRoomOfStaffId(staffId));
    }

    @GetMapping("/all-api/findAllChatOfUser")
    public ResponseEntity<List<ChatResponse>> findAllChatOfUser(@RequestParam("userId") int userId) {
        return ResponseEntity.ok(this.chatService.findAllChatOfUser(userId));
    }
    @GetMapping("/all-api/findAllChatOfStaff")
    public ResponseEntity<List<ChatResponse>> findAllChatOfStaff(@RequestParam("userId") int userId, @RequestParam("chatRoomId") int chatRoomId) {
        return ResponseEntity.ok(this.chatService.findAllChatOfStaff(userId, chatRoomId));
    }



}
