package com.order.product.repository;

import com.order.product.model.dto.TotalQuantityProductResponse;
import com.order.product.model.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    @Query(value = "select * from product_order where order_id = :orderId", nativeQuery = true)
    List<ProductOrder> findByOrderId(@Param("orderId") Integer orderId);

    @Query(value = "SELECT a.product_id AS productId, " +
            "b.product_name AS productName, " +
            "b.product_price AS productPrice, " +
            "SUM(a.quantity) AS totalQuantity, " +
            "d.brand_name AS brandName, " +
            "e.product_unit_name AS productUnitName " +
            "FROM product_order a " +
            "INNER JOIN product b ON a.product_id = b.product_id " +
            "INNER JOIN brand d ON b.brand_brand_id = d.brand_id " +
            "INNER JOIN product_unit e ON b.product_unit_product_unit_id = e.product_unit_id " +
            "WHERE a.date_created >= :startDate AND a.date_created < :endDate " +
            "GROUP BY a.product_id " +
            "ORDER BY totalQuantity DESC", nativeQuery = true)
    List<TotalQuantityProductResponse> findListTotalProductOfMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query(value = "SELECT" +
            "    MONTH(a.date_created) AS month," +
            "    SUM(a.total_price) AS totalPrice " +
            "FROM" +
            "    order_user a " +
            "WHERE" +
            "    a.date_created >= '2025-01-01'" +
            "  AND a.date_created < LAST_DAY(CURDATE()) + INTERVAL 1 DAY " +
            "GROUP BY" +
            "    MONTH(a.date_created)" +
            "ORDER BY" +
            "    MONTH(a.date_created)", nativeQuery = true)
    List<TotalQuantityProductResponse> getAllTotalPriceOfYear();
}
