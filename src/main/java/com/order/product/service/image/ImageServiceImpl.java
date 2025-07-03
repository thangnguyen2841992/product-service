package com.order.product.service.image;

import com.order.product.model.entity.Image;
import com.order.product.repository.IImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements IIMageService {
    @Autowired
    private IImageRepository imageRepository;

    @Override
    public Image saveImage(Image image) {
        return this.imageRepository.save(image);
    }
}
