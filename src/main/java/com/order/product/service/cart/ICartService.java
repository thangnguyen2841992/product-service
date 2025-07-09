package com.order.product.service.cart;

import com.order.product.model.dto.CartForm;
import com.order.product.model.dto.CartResponse;
import com.order.product.model.entity.Cart;

public interface ICartService {
    CartResponse saveNewCart(CartForm cartForm);
}
