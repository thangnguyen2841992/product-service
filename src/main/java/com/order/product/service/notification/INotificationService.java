package com.order.product.service.notification;

import com.order.product.model.entity.Notification;

import java.util.List;

public interface INotificationService {
    Notification createNotificationOrder(Notification notification);

    List<Notification> getAllNotificattionOfStaff();

    List<Notification> getAllNotificationOfUser(int userId);


}
