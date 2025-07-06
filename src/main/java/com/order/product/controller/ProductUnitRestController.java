package com.order.product.controller;

import com.order.product.model.entity.ProductUnit;
import com.order.product.service.productUnit.IProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("product-unit-api")
public class ProductUnitRestController {
    @Autowired
    private IProductUnitService productUnitService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductUnit>> getAll() {
        return new ResponseEntity<>(this.productUnitService.getAllProductUnits(), HttpStatus.OK);
    }
}
