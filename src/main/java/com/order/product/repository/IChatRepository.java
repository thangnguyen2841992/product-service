package com.order.product.repository;

import com.order.product.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChatRepository extends JpaRepository<Chat, Integer> {

    @Query(value = "select * from chat where (form_user_id = :userId or to_user_id = :userId) and chat_room_id in (select chat_room_id from chat_room where is_closed = false)order by date_created desc", nativeQuery = true)
    List<Chat> findAllChatOfUser(@Param("userId") int userId);
}
