package com.order.product.service.image;

import com.order.product.model.entity.Image;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IIMageService {
    Image saveImage(Image image);

    List<Image> findByProductId(@Param("productId") int productId);

}
