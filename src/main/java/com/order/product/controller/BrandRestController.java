package com.order.product.controller;

import com.order.product.model.entity.Brand;
import com.order.product.service.brand.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand-api")
public class BrandRestController {
    @Autowired
    private IBrandService brandService;

    @GetMapping("/getAllBrands")
    public ResponseEntity<List<Brand>> getAllBrands() {
        return new ResponseEntity<>(this.brandService.findAll(), HttpStatus.OK);
    }

}
