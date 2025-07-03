package com.order.product.service.brand;

import com.order.product.model.entity.Brand;
import com.order.product.repository.IBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements IBrandService{
    @Autowired
    private IBrandRepository brandRepository;

    @Override
    public Brand save(Brand brand) {
        return this.brandRepository.save(brand);
    }

    @Override
    public List<Brand> findAll() {
        return this.brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findByBrandName(String brandName) {
        return this.brandRepository.findByBrandName(brandName);
    }

    @Override
    public Brand findById(int id) {
        return this.brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }
}
