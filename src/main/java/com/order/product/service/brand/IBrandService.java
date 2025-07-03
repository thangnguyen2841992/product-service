package com.order.product.service.brand;

import com.order.product.model.entity.Brand;

import java.util.List;
import java.util.Optional;

public interface IBrandService {
    Brand save(Brand brand);

    List<Brand> findAll();

    Optional<Brand> findByBrandName(String brandName);

    Brand findById(int id);

}
