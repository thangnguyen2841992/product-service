package com.order.product.repository;

import com.order.product.model.entity.OrderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<OrderUser,Integer> {
}
