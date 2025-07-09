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

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private IProductCartRepository productCartRepository;

    @Autowired
    private ICartRepository cartRepository;


    @Override
    public CartResponse saveNewCart(CartForm cartForm) {
       Cart newCartSave =  this.cartRepository.save(new Cart(cartForm.getUserId(), new Date()));
       List<ProductCart> productCartList = new ArrayList<>();
       for (ProductCartForm product : cartForm.getProducts()) {
           ProductCart productCart = new ProductCart();
           productCart.setProductId(product.getProductId());
           productCart.setQuantity(product.getQuantity());
           productCart.setDateCreated(new Date());
           productCart.setCartId(newCartSave.getCartId());
           productCartList.add(this.productCartRepository.save(productCart));
       }
       CartResponse cartResponse = new CartResponse();
       cartResponse.setCartId(newCartSave.getCartId());
       cartResponse.setUserId(newCartSave.getUserId());
       cartResponse.setDateCreated(newCartSave.getDateCreated());
       cartResponse.setProductCartList(productCartList);
       return cartResponse;
    }
}
