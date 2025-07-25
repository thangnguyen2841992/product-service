package com.order.product.model.dto;

import com.order.product.model.entity.Brand;
import com.order.product.model.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductForm {

    private int productId;

    private String productName;

    private double productPrice;

    private int quantity;

    private String description;

    private int productUnitId;

    private Date dateCreated;

    private int point;

    private int brandId;

    private String[] imageLinks;
    private Image[] imageList;
}
