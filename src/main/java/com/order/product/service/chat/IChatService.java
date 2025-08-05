package com.order.product.service.chat;

import com.order.product.model.dto.ChatApproval;
import com.order.product.model.entity.Chat;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IChatService {
    Chat addChat(Chat chat);

    Chat approvalChat(ChatApproval chatApproval);
    List<Chat> findAllChatOfUser(int processId);


}
