package com.order.product.controller;

import com.order.product.model.entity.Image;
import com.order.product.service.image.IIMageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image-api")
public class ImageRestController {
    @Autowired
    private IIMageService imageService;

    @GetMapping("/getAllImagesOfProduct")
    public ResponseEntity<List<Image>> getAllImagesOfProduct(@RequestParam(name = "productId") int productId) {
        return new ResponseEntity<>(this.imageService.findByProductId(productId), HttpStatus.OK);
    }
}
