package com.order.product.service.product;

import com.order.product.model.dto.ProductForm;
import com.order.product.model.entity.Brand;
import com.order.product.model.entity.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductService {
    Product saveProduct(ProductForm productForm);
    List<Product> getAllProducts();
    List<Product> getAllProductOfBrand(int brandId);
    Product getProductById(int id);
}
