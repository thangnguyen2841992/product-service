package com.order.product.repository;

import com.order.product.model.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductOrderRepository extends JpaRepository<ProductOrder,Integer> {
}
