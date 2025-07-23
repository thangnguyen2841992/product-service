package com.order.product.service.notification;

import com.order.product.model.entity.Notification;
import com.order.product.repository.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public Notification createNotificationOrder(Notification notification) {
        return this.notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotificattionOfStaff() {
        return this.notificationRepository.getAllNotificattionOfStaff();
    }

    @Override
    public List<Notification> getAllNotificationOfUser(int userId) {
        return this.notificationRepository.getAllNotificationOfUser(userId);
    }
}
