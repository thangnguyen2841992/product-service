package com.order.product.service.image;

import com.order.product.model.entity.Image;
import com.order.product.repository.IImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageServiceImpl implements IIMageService {
    @Autowired
    private IImageRepository imageRepository;

    @Override
    public Image saveImage(Image image) {
        return this.imageRepository.save(image);
    }

    @Override
    public List<Image> findByProductId(int productId) {
        return this.imageRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteByImageIdIn(List<Integer> imageIds) {
        this.imageRepository.deleteByImageIdIn(imageIds);
    }

    @Override
    public void deleteImageById(int id) {
        this.imageRepository.deleteById(id);
    }
}
