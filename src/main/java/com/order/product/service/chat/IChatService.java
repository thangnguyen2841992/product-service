package com.order.product.service.chat;

import com.order.product.model.dto.ChatRequest;
import com.order.product.model.dto.WaitingChatResponse;
import com.order.product.model.entity.ChatRoom;
import com.order.product.model.entity.WaitingChat;

import java.util.List;

public interface IChatService {
   void addChat(ChatRequest chatRequest);
   void addChatRoom(ChatRequest chatRequest);

   List<WaitingChatResponse> findWaitingChatByDeleted();



}
