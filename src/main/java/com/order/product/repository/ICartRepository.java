package com.order.product.repository;

import com.order.product.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Integer> {

    @Query(value = "select * from cart where user_id = :userId", nativeQuery = true)
    Optional<Cart> findCartByUserId( @Param("userId") int userId);
}
