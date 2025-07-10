package com.order.product.controller;

import com.order.product.model.dto.ProductForm;
import com.order.product.model.entity.Product;
import com.order.product.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff-api")
public class StaffController {
    @Autowired
    private IProductService productService;

    @PostMapping("/createNewProduct")
    public ResponseEntity<Product> createNewProduct(@RequestBody ProductForm productForm) {
        return new ResponseEntity<>(this.productService.saveProduct(productForm), HttpStatus.CREATED);
    }
    @PostMapping("/updateProduct")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductForm productForm) {
        return new ResponseEntity<>(this.productService.updateProduct(productForm), HttpStatus.OK);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(this.productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/getAllProductsOfBrand")
    public ResponseEntity<List<Product>> getAllProductsOfBrand(@RequestParam("brandId") int brandId) {
        return new ResponseEntity<>(this.productService.getAllProductOfBrand(brandId), HttpStatus.OK);
    }

    @GetMapping("/getProductById")
    public ResponseEntity<Product> getProductById(@RequestParam("productId") int id) {
        return new ResponseEntity<>(this.productService.getProductById(id), HttpStatus.OK);
    }

    @PostMapping("/deleteProduct")
    public ResponseEntity<Product> deleteProduct(@RequestParam("productId") int id) {
        return new ResponseEntity<>(this.productService.deleteProduct(id), HttpStatus.OK);
    }
}
