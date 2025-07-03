package com.order.product.service.productUnit;

import com.order.product.model.entity.ProductUnit;
import com.order.product.repository.IProductUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductUnitServiceImpl implements IProductUnitService {
    @Autowired
    private IProductUnitRepository iProductUnitRepository;

    @Override
    public List<ProductUnit> getAllProductUnits() {
        return this.iProductUnitRepository.findAll();
    }

    @Override
    public ProductUnit save(ProductUnit productUnit) {
        return this.iProductUnitRepository.save(productUnit);
    }

    @Override
    public Optional<ProductUnit> getProductUnitById(int id) {
        return this.iProductUnitRepository.findById(id);
    }
}
