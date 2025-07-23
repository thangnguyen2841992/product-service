package com.order.product.repository;

import com.order.product.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notification,Integer> {

    @Query(value = "select * from notification where is_staff = false and to_user_id = :userId order by date_created desc", nativeQuery = true)
    List<Notification> getAllNotificationOfUser(@Param("userId") int userId);

    @Query(value = "select * from notification where is_staff = true order by date_created desc", nativeQuery = true)
    List<Notification> getAllNotificattionOfStaff();
}
