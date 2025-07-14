package com.order.product.service.cart;

import com.order.product.model.dto.CartForm;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.entity.Cart;
import com.order.product.model.entity.ProductCart;

import java.util.List;

public interface ICartService {
    CartResponse saveNewCart(CartForm cartForm);
    List<ProductCart> findProductCartByUserId(int userId);


}
