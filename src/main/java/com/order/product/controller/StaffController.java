package com.order.product.controller;

import com.order.product.model.dto.OrderResponse;
import com.order.product.model.dto.ProductForm;
import com.order.product.model.entity.Notification;
import com.order.product.model.entity.OrderUser;
import com.order.product.model.entity.Product;
import com.order.product.service.notification.INotificationService;
import com.order.product.service.order.IOrderService;
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

    @Autowired
    private IOrderService orderService;

    @Autowired
    private INotificationService notificationService;

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

    @GetMapping("/getAllOrder")
    public ResponseEntity<List<OrderResponse>> getAllOrder() {
        return new ResponseEntity<>(this.orderService.getAllOrder(), HttpStatus.OK);
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

    @PostMapping("/deleteMultipleProducts")
    public ResponseEntity<?> deleteMultipleProducts(@RequestParam("productIds") Integer[] productIds) {
        this.productService.deleteMultipleProducts(productIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/uploadProducts")
    public ResponseEntity<List<Product>> uploadProducts(@RequestBody ProductForm[] productForm) {
        return new ResponseEntity<>(this.productService.uploadProducts(productForm), HttpStatus.CREATED);
    }

    @PostMapping("/uploadEditProducts")
    public ResponseEntity<List<Product>> uploadEditProducts(@RequestBody ProductForm[] productForm) {
        return new ResponseEntity<>(this.productService.uploadEditProducts(productForm), HttpStatus.CREATED);
    }

    @PostMapping("/process-order")
    public ResponseEntity<OrderUser> processOrder(@RequestParam("orderId") int orderId) {
        return new ResponseEntity<>(this.orderService.processOrder(orderId), HttpStatus.OK);
    }
    @PostMapping("/done-order")
    public ResponseEntity<OrderUser> doneOrder(@RequestParam("orderId") int orderId) {
        return new ResponseEntity<>(this.orderService.doneOrder(orderId), HttpStatus.OK);
    }
    @GetMapping("/getAllNotificationsOfStaff")
    public ResponseEntity<List<Notification>> getAllNotificationsOfStaff(){
        return new ResponseEntity<>(this.notificationService.getAllNotificattionOfStaff(), HttpStatus.OK );
    }
}
