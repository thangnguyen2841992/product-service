package com.order.product.repository;

import com.order.product.model.entity.Brand;
import com.order.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "select * from product where brand_brand_id = :brandId", nativeQuery = true)
    List<Product> getAllProductOfBrand(@Param("brandId") int brandId);
}
