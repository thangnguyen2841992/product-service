package com.order.product.repository;

import com.order.product.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}
