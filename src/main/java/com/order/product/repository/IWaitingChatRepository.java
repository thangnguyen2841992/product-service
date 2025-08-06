package com.order.product.repository;

import com.order.product.model.entity.WaitingChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWaitingChatRepository  extends JpaRepository<WaitingChat, Integer> {

    Optional<WaitingChat> findWaitingChatByUserId(int userId);

    @Query(value = "select * from waiting_chat where deleted = false", nativeQuery = true)
    List<WaitingChat> findWaitingChatByDeleted();
}
