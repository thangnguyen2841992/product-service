package com.order.product.repository;

import com.order.product.model.entity.ProductCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductCartRepository extends JpaRepository<ProductCart, Integer> {
}
