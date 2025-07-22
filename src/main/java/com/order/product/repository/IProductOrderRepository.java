package com.order.product.repository;

import com.order.product.model.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    @Query(value = "select * from product_order where order_id = :orderId", nativeQuery = true)
    List<ProductOrder> findByOrderId(@Param("orderId") Integer orderId);
}
