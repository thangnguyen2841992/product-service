package com.order.product.repository;

import com.order.product.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    @Query(value = "select * from chat_room where staff_id = :staffId and is_closed = false", nativeQuery = true)
    List<ChatRoom> getAllChatRoomOfStaffId(@Param("staffId") int staffId);

    @Query(value = "select * from chat_room where form_user_id = :userId and is_closed = false", nativeQuery = true)
    Optional<ChatRoom> getChatRoomOfUserId(@Param("userId") int userId);
}
