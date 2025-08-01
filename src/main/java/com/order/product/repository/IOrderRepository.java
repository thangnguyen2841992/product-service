package com.order.product.repository;

import com.order.product.model.entity.OrderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<OrderUser,Integer> {

    @Query(value = "select * from order_user where user_id = :userId", nativeQuery = true)
    List<OrderUser> findByUserId(@Param("userId") Integer userId);
}
