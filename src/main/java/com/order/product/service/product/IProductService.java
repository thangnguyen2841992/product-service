package com.order.product.service.product;

import com.order.product.model.dto.ProductForm;
import com.order.product.model.entity.Product;

public interface IProductService {
    Product saveProduct(ProductForm productForm);
}
