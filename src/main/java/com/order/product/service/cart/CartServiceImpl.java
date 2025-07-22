package com.order.product.service.cart;

import com.order.product.model.dto.CartForm;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.MessageError;
import com.order.product.model.dto.ProductCartForm;
import com.order.product.model.entity.Cart;
import com.order.product.model.entity.Product;
import com.order.product.model.entity.ProductCart;
import com.order.product.repository.ICartRepository;
import com.order.product.repository.IProductCartRepository;
import com.order.product.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private IProductCartRepository productCartRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    @Override
    public CartResponse saveNewCart(CartForm cartForm) {
        Optional<Cart> cartOptional = this.cartRepository.findCartByUserId(cartForm.getUserId());
        CartResponse cartResponse = new CartResponse();
        if (cartOptional.isEmpty()) {
            Cart newCartSave = this.cartRepository.save(new Cart(cartForm.getUserId(), new Date()));
            List<ProductCart> productCartList = new ArrayList<>();
            ProductCart productCart = new ProductCart();
            productCart.setProductId(cartForm.getProduct().getProductId());
            productCart.setQuantity(cartForm.getProduct().getQuantity());
            productCart.setDateCreated(new Date());
            productCart.setCartId(newCartSave.getCartId());
            productCartList.add(this.productCartRepository.save(productCart));
            cartResponse.setCartId(newCartSave.getCartId());
            cartResponse.setUserId(newCartSave.getUserId());
            cartResponse.setDateCreated(newCartSave.getDateCreated());
            cartResponse.setProductCartList(productCartList);
            return cartResponse;
        } else {
            List<ProductCart> productCartList = this.productCartRepository.findAllProductCartByCartId(cartOptional.get().getCartId());
            ProductCartForm productCartForm = cartForm.getProduct();
            ProductCart existProductCart = productCartList.stream().filter(productCart -> productCart.getProductId() == cartForm.getProduct().getProductId()).findFirst().orElse(null);
            if (existProductCart != null) {
                if (existProductCart.getQuantity() != productCartForm.getQuantity()) {
                    existProductCart.setQuantity(cartForm.getProduct().getQuantity());
                    existProductCart.setDateUpdated(new Date());
                    this.productCartRepository.save(existProductCart);
                }
            } else {
                ProductCart productCartNew = new ProductCart();
                productCartNew.setProductId(productCartForm.getProductId());
                productCartNew.setQuantity(productCartForm.getQuantity());
                productCartNew.setDateCreated(new Date());
                productCartNew.setCartId(cartOptional.get().getCartId());
                this.productCartRepository.save(productCartNew);
            }

            return getCartResponseByUserId(cartForm.getUserId());
        }
    }

    @Transactional
    @Override
    public void editQuantity(ProductCartForm productCartForm) {
        Cart cart = this.cartRepository.findCartByUserId(productCartForm.getUserId()).orElseThrow(() -> new RuntimeException("Not Found"));
        List<ProductCart> productCartList = this.productCartRepository.findAllProductCartByCartId(cart.getCartId());
        Optional<ProductCart> productCartOptional = productCartList.stream().filter(productCartData -> productCartData.getProductId() == productCartForm.getProductId()).findFirst();
        if (productCartOptional.isPresent()) {
            ProductCart productCart = productCartOptional.get();
            if (productCartForm.getQuantity() == 0) {
                if (productCart.getQuantity() >= 2) {
                    productCart.setQuantity(productCart.getQuantity() - 1);
                    productCart.setDateUpdated(new Date());
                    this.productCartRepository.save(productCart);
                } else {
                    this.productCartRepository.deleteById(productCart.getProductCartId());
                }
                CartResponse cartResponse = getCartResponseByUserId(productCartForm.getUserId());
                simpMessagingTemplate.convertAndSend("/topic/cart", cartResponse);
            } else {
                Product product = this.productRepository.findById(productCartForm.getProductId()).orElse(null);
                if (product != null) {
                    if (product.getQuantity() > productCart.getQuantity()) {
                        productCart.setQuantity(productCart.getQuantity() + 1);
                        productCart.setDateUpdated(new Date());
                        this.productCartRepository.save(productCart);
                        CartResponse cartResponse = getCartResponseByUserId(productCartForm.getUserId());
                        simpMessagingTemplate.convertAndSend("/topic/cart", cartResponse);
                    } else {
                        MessageError messageError = new MessageError();
                        messageError.setMessage("Sản phẩm này chỉ còn lại " + product.getQuantity());
                        simpMessagingTemplate.convertAndSend("/topic/messageResponse", messageError);
                    }
                }
                }

        }

    }

    @Override
    public CartResponse getCartResponseByUserId(int userId) {
        CartResponse cartResponse = new CartResponse();
        Cart cart = this.cartRepository.findCartByUserId(userId).orElseThrow(() -> new RuntimeException("Not Found"));
        List<ProductCart> productCartList = this.productCartRepository.findAllProductCartByCartId(cart.getCartId());
        List<Integer> productIds = productCartList.stream()
                .map(ProductCart::getProductId)
                .collect(Collectors.toList());

        Map<Integer, Product> productMap = this.productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        long totalPrice = productCartList.stream()
                .mapToLong(productCart -> {
                    Product product = productMap.get(productCart.getProductId());
                    if (product == null) {
                        throw new RuntimeException("Not Found");
                    }
                    return (long) (product.getProductPrice() * productCart.getQuantity());
                })
                .sum();


        long totalQuantity = productCartList.stream()
                .mapToLong(ProductCart::getQuantity)
                .sum();
        cartResponse.setTotalPrice(totalPrice);
        cartResponse.setCartId(cart.getCartId());
        cartResponse.setUserId(cart.getUserId());
        cartResponse.setDateCreated(cart.getDateCreated());
        cartResponse.setProductCartList(productCartList);
        cartResponse.setTotalProduct(totalQuantity);
        return cartResponse;
    }

    @Override
    public CartResponse removeProductCart(int productCartId) {
        Optional<ProductCart> productCartOptional = this.productCartRepository.findById(productCartId);
        productCartOptional.ifPresent(productCart -> this.productCartRepository.deleteById(productCart.getProductCartId()));
        Cart cart = this.cartRepository.findById(productCartOptional.get().getCartId()).orElseThrow(() -> new RuntimeException("Not Found"));
        return getCartResponseByUserId(cart.getUserId());
    }

    @Override
    public CartResponse removeAllProductCart(int cartId) {
        this.productCartRepository.deleteAllProductOfCart(cartId);
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Not Found"));
        return getCartResponseByUserId(cart.getUserId());
    }


}
