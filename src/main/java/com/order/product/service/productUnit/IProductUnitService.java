package com.order.product.service.productUnit;

import com.order.product.model.entity.ProductUnit;

import java.util.List;
import java.util.Optional;

public interface IProductUnitService {
    List<ProductUnit> getAllProductUnits();

    ProductUnit save(ProductUnit productUnit);

    Optional<ProductUnit> getProductUnitById(int id);
}
