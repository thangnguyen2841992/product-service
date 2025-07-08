package com.order.product.service.product;

import com.order.product.model.dto.ProductForm;
import com.order.product.model.entity.Brand;
import com.order.product.model.entity.Image;
import com.order.product.model.entity.Product;
import com.order.product.model.entity.ProductUnit;
import com.order.product.repository.IImageRepository;
import com.order.product.repository.IProductRepository;
import com.order.product.service.brand.IBrandService;
import com.order.product.service.image.IIMageService;
import com.order.product.service.productUnit.IProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private IProductUnitService productUnitService;

    @Autowired
    private IIMageService iiMageService;
    
    @Autowired
    private IImageRepository imageRepository;



    @Override
    public Product saveProduct(ProductForm productForm) {
        Product product = new Product();
        product.setProductName(productForm.getProductName());
        product.setQuantity(productForm.getQuantity());
        product.setProductPrice(productForm.getProductPrice());
        product.setBrand(getBrand(productForm.getBrandId()));
        product.setDescription(productForm.getDescription());
        product.setProductUnit(getProductUnit(productForm.getProductUnitId()));
        product.setPoint(productForm.getPoint());
        product.setDateCreated(new Date());
        String[] images = productForm.getImageLinks();
        Product newProductSave = productRepository.save(product);
        for (String image : images) {
            this.iiMageService.saveImage(new Image(image, new Date(), "",newProductSave));
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductOfBrand(int brandId) {
        return this.productRepository.getAllProductOfBrand(brandId);
    }

    @Override
    public Product getProductById(int id) {
        return this.productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product updateProduct(ProductForm productForm) {
        Optional<Product> productOptional = this.productRepository.findById(productForm.getProductId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setProductName(productForm.getProductName());
            product.setQuantity(productForm.getQuantity());
            product.setProductPrice(productForm.getProductPrice());
            product.setBrand(getBrand(productForm.getBrandId()));
            product.setDescription(productForm.getDescription());
            product.setProductUnit(getProductUnit(productForm.getProductUnitId()));
            product.setPoint(productForm.getPoint());
            product.setDateUpdated(new Date());
            this.productRepository.save(product);
            List<Image> imageList = this.iiMageService.findByProductId(product.getProductId());
            editImages(productForm, imageList);
            return product;
        } else {
            return null;
        }
    }

    private void editImages(ProductForm productForm, List<Image> imageList) {
        List<Integer> currentImageIds = imageList.stream()
                .map(Image::getImageId)
                .toList();

        List<Integer> newImageIds = Arrays.stream(productForm.getImageList())
                .map(Image::getImageId)
                .toList();
        List<Integer> idsToDelete = currentImageIds.stream()
                .filter(id -> !newImageIds.contains(id))
                .collect(Collectors.toList());

        if (!idsToDelete.isEmpty()) {
            this.iiMageService.deleteByImageIdIn(idsToDelete);
        }
        List<Image> imagesToAdd = Arrays.stream(productForm.getImageList())
                .filter(image -> image.getImageId() == 0)
                .peek(image -> {
                    image.setDateCreated(new Date());
                })
                .collect(Collectors.toList());

        if (!imagesToAdd.isEmpty()) {
            imageRepository.saveAll(imagesToAdd);
        }
    }


    private Brand getBrand(int brandId) {
        return this.brandService.findById(brandId);
    }

    private ProductUnit getProductUnit(int productUnitId) {
        return this.productUnitService.getProductUnitById(productUnitId).orElseThrow(() -> new RuntimeException("Not Found"));
    }
}
