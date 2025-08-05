package com.order.product.service.chat;

import com.order.product.model.dto.ChatApproval;
import com.order.product.model.entity.Chat;
import com.order.product.model.entity.Notification;
import com.order.product.repository.ChatRepository;
import com.order.product.service.notification.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private INotificationService notificationService;

    @Override
    public Chat addChat(Chat chat) {
        chat.setDateCreated(new Date());
        Chat chatSave = this.chatRepository.save(chat);
        if (!chatSave.isReplied()) {
            Notification notification = new Notification();
            notification.setChatId(chatSave.getChatId());
            notification.setStaff(true);
            notification.setMessage("Có 1 tin nhắn mới từ khách hàng.");
            notification.setDateCreated(new Date());
            Notification notificationSave = notificationService.createNotificationOrder(notification);
            simpMessagingTemplate.convertAndSend("/topic/staffNotification", notificationSave);
        }
        return chatSave;
    }

    @Override
    public Chat approvalChat(ChatApproval chatApproval) {
        Chat chat = this.chatRepository.findById(chatApproval.getChatId()).orElse(null);
        if (chat != null) {
            if (!chat.isReplied()) {
                chat.setReplied(true);
                chat.setStaffId(chatApproval.getUserId());
                chat.setProcessId(chat.getChatId());
                Chat chatEdit = chatRepository.save(chat);

                simpMessagingTemplate.convertAndSend("/topic/approvalChat", chatEdit);
                return chatEdit;
            } else {
                simpMessagingTemplate.convertAndSend("/topic/approvalChat", chat);
                return chat;
            }
        }
        simpMessagingTemplate.convertAndSend("/topic/approvalChat", new Chat());
        return null;
    }

    @Override
    public List<Chat> findAllChatOfUser(int processId) {
        return this.chatRepository.findAllChatOfUser(processId);
    }
}
