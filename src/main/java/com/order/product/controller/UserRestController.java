package com.order.product.controller;

import com.order.product.model.entity.Notification;
import com.order.product.model.entity.Product;
import com.order.product.service.notification.INotificationService;
import com.order.product.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-api")
public class UserRestController {
    @Autowired
    private IProductService productService;

    @Autowired
    private INotificationService   notificationService;

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(this.productService.getAllProductsUser(), HttpStatus.OK );
    }
    @GetMapping("/getProductById")
    public ResponseEntity<Product> getProductById(@RequestParam("productId") int id) {
        return new ResponseEntity<>(this.productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/getAllNotificationsOfUser")
    public ResponseEntity<List<Notification>> getAllNotificationsOfUser(@RequestParam("userId") int userId){
        return new ResponseEntity<>(this.notificationService.getAllNotificationOfUser(userId), HttpStatus.OK );
    }
}
