package com.order.product.repository;

import com.order.product.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "select * from image where product_product_id = :productId", nativeQuery = true)
    List<Image> findByProductId(@Param("productId") int productId);
}
