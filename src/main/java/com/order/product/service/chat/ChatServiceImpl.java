package com.order.product.service.chat;

import com.order.product.model.dto.ChatRequest;
import com.order.product.model.dto.ChatResponse;
import com.order.product.model.dto.ChatRoomResponse;
import com.order.product.model.dto.WaitingChatResponse;
import com.order.product.model.entity.*;
import com.order.product.repository.IChatRepository;
import com.order.product.repository.IChatRoomRepository;
import com.order.product.repository.IUserRepository;
import com.order.product.repository.IWaitingChatRepository;
import com.order.product.service.notification.INotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    private IChatRepository chatRepository;

    @Autowired
    private IChatRoomRepository chatRoomRepository;

    @Autowired
    private IWaitingChatRepository waitingChatRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IUserRepository userRepository;

    @Override
    @Transactional
    public void addChat(ChatRequest chat) {
        WaitingChat waitingChat = waitingChatRepository.findWaitingChatByUserId(chat.getFormUserId()).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.getChatRoomOfUserId(chat.getFormUserId()).orElse(null);
        if (waitingChat == null && chatRoom == null) {
            WaitingChat newWaitingChat = new WaitingChat();
            newWaitingChat.setUserId(chat.getFormUserId());
            newWaitingChat.setContent(chat.getContent());
            newWaitingChat.setDeleted(false);
            newWaitingChat.setDateCreated(new Date());
            WaitingChat newChatSave = this.waitingChatRepository.save(newWaitingChat);
            ChatResponse chatResponse = new ChatResponse();
            userRepository.findById(newChatSave.getUserId()).ifPresent(user -> chatResponse.setFormUserName(user.getUsername()));
            chatResponse.setFormUserId(newChatSave.getUserId());
            chatResponse.setDateCreated(newChatSave.getDateCreated());
            simpMessagingTemplate.convertAndSend("/topic/chat", chatResponse);

            Notification notification = new Notification();
            notification.setWaitingChatId(newChatSave.getMessageId());
            notification.setStaff(true);
            notification.setMessage("Có 1 tin nhắn mới từ khách hàng.");
            notification.setDateCreated(new Date());
            notification.setChat(true);
            Notification notificationSave = notificationService.createNotificationOrder(notification);
            simpMessagingTemplate.convertAndSend("/topic/staffNotification", notificationSave);
        } else if (chatRoom != null) {
            Chat newChat = new Chat();
            newChat.setContent(chat.getContent());
            newChat.setDateCreated(new Date());
            newChat.setFormUserId(chat.getFormUserId());
            newChat.setToUserId(chatRoom.getStaffId());
            newChat.setChatRoomId(chatRoom.getChatRoomId());
            this.chatRepository.save(newChat);
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setFormUserId(newChat.getFormUserId());
            chatResponse.setToUserId(newChat.getToUserId());
            simpMessagingTemplate.convertAndSend("/topic/chat", chatResponse);
        }
    }

    @Override
    @Transactional
    public void addChatRoom(ChatRequest chatRequest) {
        WaitingChat waitingChat = this.waitingChatRepository.findById(chatRequest.getMessageId()).orElse(null);
        if (waitingChat != null) {
            waitingChat.setDeleted(true);
            this.waitingChatRepository.save(waitingChat);

            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setDateCreated(new Date());
            chatRoom.setClosed(false);
            chatRoom.setFormUserId(waitingChat.getUserId());
            chatRoom.setStaffId(chatRequest.getStaffId());
            ChatRoom chatRoomSave = this.chatRoomRepository.save(chatRoom);

            Chat chat = new Chat();
            chat.setFormUserId(chatRoom.getFormUserId());
            chat.setToUserId(chatRoom.getStaffId());
            chat.setContent(waitingChat.getContent());
            chat.setDateCreated(waitingChat.getDateCreated());
            chat.setChatRoomId(chatRoomSave.getChatRoomId());
            this.chatRepository.save(chat);

            Chat chatStaff = new Chat();
            chatStaff.setFormUserId(chatRoom.getStaffId());
            chatStaff.setToUserId(chatRoom.getFormUserId());
            chatStaff.setContent("Xin chào quý khách.");
            chatStaff.setDateCreated(new Date());
            chatStaff.setChatRoomId(chatRoomSave.getChatRoomId());
            this.chatRepository.save(chatStaff);

            ChatResponse chatResponse = new ChatResponse();
            userRepository.findById(chat.getFormUserId()).ifPresent(user -> chatResponse.setFormUserName(user.getFirstName() + " " + user.getLastName()));
            userRepository.findById(chat.getToUserId()).ifPresent(user -> chatResponse.setToUserName(user.getFirstName() + " " + user.getLastName()));
            chatResponse.setFormUserId(chat.getFormUserId());
            chatResponse.setToUserId(chat.getToUserId());
            chatResponse.setDateCreated(chat.getDateCreated());
            chatResponse.setShowPopup(true);
            simpMessagingTemplate.convertAndSend("/topic/chat", chatResponse);
        }
    }

    @Override
    public List<WaitingChatResponse> findWaitingChatByDeleted() {
        List<WaitingChat> waitingChats = waitingChatRepository.findWaitingChatByDeleted();

        // Lấy danh sách userId và staffAssignId để truy vấn một lần
        Set<Integer> userIds = waitingChats.stream()
                .map(WaitingChat::getUserId)
                .collect(Collectors.toSet());
        Set<Integer> staffAssignIds = waitingChats.stream()
                .map(WaitingChat::getStaffAssignId)
                .collect(Collectors.toSet());

        // Lấy danh sách người dùng một lần
        Map<Integer, String> userNames = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, user -> user.getFirstName() + " " + user.getLastName()));

        Map<Integer, String> staffAssignNames = userRepository.findAllById(staffAssignIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, user -> user.getFirstName() + " " + user.getLastName()));

        return waitingChats.stream().map(waitingChat -> {
            WaitingChatResponse waitingChatResponse = new WaitingChatResponse();
            waitingChatResponse.setContent(waitingChat.getContent());
            waitingChatResponse.setMessageId(waitingChat.getMessageId());
            waitingChatResponse.setDeleted(waitingChat.isDeleted());
            waitingChatResponse.setDateCreated(waitingChat.getDateCreated());
            waitingChatResponse.setUserId(waitingChat.getUserId());
            waitingChatResponse.setUserName(userNames.get(waitingChat.getUserId()));
            waitingChatResponse.setStaffAssignName(staffAssignNames.get(waitingChat.getStaffAssignId()));
            return waitingChatResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomResponse> getAllChatRoomOfStaffId(int staffId) {
        List<ChatRoom> chatRoomList = this.chatRoomRepository.getAllChatRoomOfStaffId(staffId);

        // Lấy danh sách userId và staffAssignId để truy vấn một lần
        Set<Integer> userIds = chatRoomList.stream()
                .map(ChatRoom::getFormUserId)
                .collect(Collectors.toSet());
        Set<Integer> staffAssignIds = chatRoomList.stream()
                .map(ChatRoom::getStaffId)
                .collect(Collectors.toSet());

        // Lấy danh sách người dùng một lần
        Map<Integer, String> userNames = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, user -> user.getFirstName() + " " + user.getLastName()));

        Map<Integer, String> staffAssignNames = userRepository.findAllById(staffAssignIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, user -> user.getFirstName() + " " + user.getLastName()));

        return chatRoomList.stream().map(chatRoom -> {
            ChatRoomResponse chatRoomResponse = new ChatRoomResponse();
            chatRoomResponse.setChatRoomId(chatRoom.getChatRoomId());
            chatRoomResponse.setClosed(chatRoom.isClosed());
            chatRoomResponse.setStaffId(chatRoom.getStaffId());
            chatRoomResponse.setFormUserId(chatRoom.getFormUserId());
            chatRoomResponse.setDateCreated(chatRoom.getDateCreated());
            chatRoomResponse.setFormUsername(userNames.get(chatRoom.getFormUserId()));
            chatRoomResponse.setStaffName(staffAssignNames.get(chatRoom.getStaffId()));
            return chatRoomResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChatResponse> findAllChatOfUser(int userId) {
        List<Chat> chats = this.chatRepository.findAllChatOfUser(userId);

        Set<Integer> formUserIds = chats.stream()
                .map(Chat::getFormUserId)
                .collect(Collectors.toSet());
        Set<Integer> toUserIds = chats.stream()
                .map(Chat::getToUserId)
                .collect(Collectors.toSet());

        Map<Integer, String> formUsers = userRepository.findAllById(formUserIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, user -> user.getFirstName() + " " + user.getLastName()));

        Map<Integer, String> toUsers = userRepository.findAllById(toUserIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, user -> user.getFirstName() + " " + user.getLastName()));

        return chats.stream().map(chat -> {
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setShowPopup(true);
            chatResponse.setChatRoomId(chat.getChatRoomId());
            chatResponse.setContent(chat.getContent());
            chatResponse.setDateCreated(chat.getDateCreated());
            chatResponse.setFormUserId(chat.getFormUserId());
            chatResponse.setToUserId(chat.getToUserId());
            chatResponse.setFormUserName(formUsers.get(chat.getFormUserId()));
            chatResponse.setToUserName(toUsers.get(chat.getToUserId()));
            return chatResponse;
        }).collect(Collectors.toList());
    }

}
