package com.order.product.service.order;

import com.order.product.model.dto.CartResponse;
import com.order.product.model.dto.MessageOrder;
import com.order.product.model.dto.OrderForm;
import com.order.product.model.dto.OrderResponse;
import com.order.product.model.entity.*;
import com.order.product.repository.*;
import com.order.product.service.cart.ICartService;
import com.order.product.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private IProductOrderRepository productOrderRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IDeliveryRepository deliveryRepository;

    @Autowired
    private IProductCartRepository productCartRepository;

    @Autowired
    private ICartService cartService;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional
    public CartResponse createNewOrder(OrderForm orderForm) {
        OrderUser orderUser = new OrderUser();
        orderUser.setDateCreated(new Date());
        orderUser.setDeliveryId(orderForm.getDeliveryId());
        orderUser.setPaymentId(orderForm.getPaymentId());
        orderUser.setPaymented(orderForm.getPaymentId() == 2);
        orderUser.setDelete(false);
        orderUser.setProcess(false);
        orderUser.setDone(false);
        orderUser.setTotalPrice(orderForm.getTotalPrice());
        orderUser.setDescription(orderForm.getDescription());
        orderUser.setUserId(orderForm.getUserId());
        OrderUser orderUserSave = orderRepository.save(orderUser);
        List<ProductCart> productCartList = this.productCartRepository.findAllProductCartByCartId(orderForm.getCartId());
        int totalProduct = productCartList.stream()
                .mapToInt(ProductCart::getQuantity)
                .sum();

        List<Integer> productIds = productCartList.stream()
                .map(ProductCart::getProductId)
                .collect(Collectors.toList());
        List<Product> productList = productRepository.findAllById(productIds);
        Map<Integer, Product> productMap = productList
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
        List<ProductOrder> productOrders = productCartList.stream()
                .map(productCart -> {
                    ProductOrder productOrder = new ProductOrder();
                    productOrder.setDateCreated(new Date());
                    productOrder.setOrderId(orderUserSave.getOrderId());
                    productOrder.setQuantity(productCart.getQuantity());
                    productOrder.setProductId(productCart.getProductId()); // Sửa lại setProductId
                    return productOrder;
                })
                .collect(Collectors.toList());
        this.productOrderRepository.saveAll(productOrders);
        for (ProductCart productCart : productCartList) {
            Product product = productMap.get(productCart.getProductId());
            if (product != null) {
                product.setQuantity(product.getQuantity() - productCart.getQuantity());
            }
        }
        this.productRepository.saveAll(productList);
        this.productCartRepository.deleteAll(productCartList);
        MessageOrder messageOrder = new MessageOrder();
        User user = this.userRepository.findById(orderUser.getUserId()).orElse(null);
        if (user != null) {
            messageOrder.setUserId(orderUserSave.getUserId());
            messageOrder.setFullName(user.getFirstName() + " " + user.getLastName());
            messageOrder.setGender(user.getGender());
            messageOrder.setTotalProduct(totalProduct);
            messageOrder.setTotalPrice(totalPrice);
            messageOrder.setToEmail(user.getEmail());
            messageOrder.setOrderId(orderUserSave.getOrderId());
            kafkaTemplate.send("send-email-order", messageOrder);
        }

        return cartService.getCartResponseByUserId(orderUser.getUserId());
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        List<OrderUser> orderUserList = this.orderRepository.findAll();
        return orderUserList.stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse getOrderResponse(OrderUser orderUser) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(orderUser.getOrderId());
        orderResponse.setDateCreated(orderUser.getDateCreated());
        orderResponse.setDateUpdated(orderUser.getDateUpdated());
        User user = this.userRepository.findById(orderUser.getUserId()).orElse(null);
        orderResponse.setUserId(user);
        orderResponse.setTotalPrice(orderUser.getTotalPrice());
        orderResponse.setDescription(orderUser.getDescription());
        orderResponse.setDone(orderUser.isDone());
        orderResponse.setProcess(orderUser.isProcess());
        orderResponse.setDelete(orderUser.isDelete());
        orderResponse.setPaymented(orderUser.isPaymented());
        this.deliveryRepository.findById(orderUser.getDeliveryId()).ifPresent(orderResponse::setDeliveryId);
        this.paymentRepository.findById(orderUser.getPaymentId()).ifPresent(orderResponse::setPaymentId);
        List<ProductOrder> productOrders = this.productOrderRepository.findByOrderId(orderUser.getOrderId());
        orderResponse.setProductOrder(productOrders);
        return orderResponse;
    }
}
