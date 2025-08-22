package com.order.product.model.entity;

import com.order.product.model.dto.TotalQuantityProductResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedNativeQuery;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
//@NamedNativeQuery(
//        name = "TotalQuantityProductResponse.findAll",
//        query = "SELECT a.product_id AS productId, " +
//                "b.product_name AS productName, " +
//                "SUM(a.quantity) AS totalQuantity, " +
//                "MIN(c.image_link) AS imageLink " +
//                "FROM product_order a " +
//                "INNER JOIN product b ON a.product_id = b.product_id " +
//                "INNER JOIN image c ON a.product_id = c.product_product_id " +
//                "WHERE a.date_created >= '2025-07-01' AND a.date_created < '2025-08-01' " +
//                "GROUP BY a.product_id " +
//                "ORDER BY totalQuantity DESC",
//        resultSetMapping = "TotalQuantityProductResponseMapping"
//)
//@SqlResultSetMapping(
//        name = "TotalQuantityProductResponseMapping",
//        classes = @ConstructorResult(
//                targetClass = TotalQuantityProductResponse.class,
//                columns = {
//                        @ColumnResult(name = "productId", type = Long.class),
//                        @ColumnResult(name = "productName", type = String.class),
//                        @ColumnResult(name = "totalQuantity", type = Long.class),
//                        @ColumnResult(name = "imageLink", type = String.class)
//                }
//        )
//)
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productOrderId;

    private int orderId;

    private int quantity;

    private int productId;

    private Date dateCreated;

    private boolean isDelete;
}
