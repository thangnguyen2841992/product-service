package com.order.product.service.cart;

import com.order.product.model.dto.CartForm;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.ProductCartForm;
import com.order.product.model.entity.Cart;
import com.order.product.model.entity.Product;
import com.order.product.model.entity.ProductCart;
import com.order.product.repository.ICartRepository;
import com.order.product.repository.IProductCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private IProductCartRepository productCartRepository;

    @Autowired
    private ICartRepository cartRepository;


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
            if (existProductCart != null ) {
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
                ProductCart newProductCartSave = this.productCartRepository.save(productCartNew);
            }
            getAllProductCartOfCart(cartOptional, cartResponse);
            return cartResponse;
        }
    }

    @Override
    public List<ProductCart> findProductCartByUserId(int userId) {
        return this.productCartRepository.findProductCartByUserId(userId);
    }

    private void getAllProductCartOfCart(Optional<Cart> cartOptional, CartResponse cartResponse) {
        List<ProductCart> productCartList;
        productCartList = this.productCartRepository.findAllProductCartByCartId(cartOptional.get().getCartId());
        cartResponse.setCartId(cartOptional.get().getCartId());
        cartResponse.setUserId(cartOptional.get().getUserId());
        cartResponse.setDateCreated(cartOptional.get().getDateCreated());
        cartResponse.setProductCartList(productCartList);
    }
}
