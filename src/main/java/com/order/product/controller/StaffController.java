package com.order.product.controller;

import com.order.product.model.dto.ProductForm;
import com.order.product.model.entity.Product;
import com.order.product.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff-api")
public class StaffController {
    @Autowired
    private IProductService productService;

    @PostMapping("/createNewProduct")
    public ResponseEntity<Product> createNewProduct(@RequestBody ProductForm productForm) {
        return new ResponseEntity<>(this.productService.saveProduct(productForm), HttpStatus.CREATED);
    }
}
