package com.order.product.repository;

import com.order.product.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query(value = "select * from chat where process_id = :processId", nativeQuery = true)
    List<Chat> findAllChatOfUser(@Param("processId") int processId);
}
