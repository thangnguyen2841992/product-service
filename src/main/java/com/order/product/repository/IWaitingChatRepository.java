package com.order.product.repository;

import com.order.product.model.entity.WaitingChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWaitingChatRepository  extends JpaRepository<WaitingChat, Integer> {

    @Query(value = "select * from waiting_chat where user_id = :userId and deleted = false ", nativeQuery = true)
    Optional<WaitingChat> findWaitingChatByUserId(@Param("userId") int userId);

    @Query(value = "select * from waiting_chat where deleted = false", nativeQuery = true)
    List<WaitingChat> findWaitingChatByDeleted();
}
