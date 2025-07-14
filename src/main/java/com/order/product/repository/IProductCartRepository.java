package com.order.product.repository;

import com.order.product.model.entity.ProductCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductCartRepository extends JpaRepository<ProductCart, Integer> {

    @Query(value = "select * from product_cart where cart_id = :cartId", nativeQuery = true)
    List<ProductCart> findAllProductCartByCartId(@Param("cartId") int cartId);

    @Query(value = "select * from product_cart where cart_id = (select cart_id form cart where user_id = :user_id)", nativeQuery = true)
    List<ProductCart> findProductCartByUserId(int userId);

}
