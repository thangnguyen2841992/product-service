package com.order.product.repository;

import com.order.product.model.entity.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductUnitRepository extends JpaRepository<ProductUnit, Integer> {
}
