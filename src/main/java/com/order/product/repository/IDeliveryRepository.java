package com.order.product.repository;

import com.order.product.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeliveryRepository extends JpaRepository<Delivery,Integer> {
}
