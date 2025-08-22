package com.order.product.service.order;

import com.order.product.model.dto.*;
import com.order.product.model.entity.*;
import com.order.product.repository.*;
import com.order.product.service.cart.ICartService;
import com.order.product.service.notification.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    @Autowired
    private INotificationService notificationService;


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
                if (product.getQuantity() >= productCart.getQuantity()) {
                    product.setQuantity(product.getQuantity() - productCart.getQuantity());
                } else {
                    MessageError messageError = new MessageError();
                    messageError.setToUserId(orderForm.getUserId());
                    messageError.setError(true);
                    if (product.getQuantity() > 0) {
                        messageError.setMessage("Sản phẩm " + product.getProductName() + " chỉ còn lại " + product.getQuantity() + " sản phẩm.");
                    } else {
                        messageError.setMessage("Sản phẩm " + product.getProductName() + " đã hết hàng.");
                    }
                    simpMessagingTemplate.convertAndSend("/topic/order", messageError);
                    return null;
                }
            }
        }
        this.productRepository.saveAll(productList);
        this.productCartRepository.deleteAll(productCartList);
        Notification notification = new Notification();
        notification.setOrderId(orderUser.getOrderId());
        notification.setToUserId(orderUserSave.getUserId());
        notification.setDateCreated(new Date());
        notification.setStaff(false);
        notification.setMessage("Bạn đã đặt đơn hàng có mã số #"+ orderUser.getOrderId()+ ". Chúng tôi sẽ gưi đơn hàng của bạn sớm" );
        Notification notificationSave = notificationService.createNotificationOrder(notification);

        Notification notificationStaff = new Notification();
        notificationStaff.setOrderId(orderUser.getOrderId());
        notificationStaff.setDateCreated(new Date());
        notificationStaff.setStaff(true);
        notificationStaff.setMessage("Có thêm 1 đơn hàng mã số #"+ orderUser.getOrderId());
        Notification notificationStaffSave = notificationService.createNotificationOrder(notificationStaff);
        simpMessagingTemplate.convertAndSend("/topic/staffNotification", notificationStaffSave);

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

    @Override
    public List<OrderResponse> getAllOrderOfUser(int userId) {
        List<OrderUser> orderUserList = this.orderRepository.findByUserId(userId);
        return orderUserList.stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderUser processOrder(int orderId) {
        OrderUser orderUser = this.orderRepository.findById(orderId).orElse(null);
        if (orderUser != null) {
            if (!orderUser.isProcess()) {
                orderUser.setProcess(true);
            }
            this.orderRepository.save(orderUser);
        }
        Notification notification = new Notification();
        assert orderUser != null;
        notification.setOrderId(orderUser.getOrderId());
        notification.setToUserId(orderUser.getUserId());
        notification.setDateCreated(new Date());
        notification.setStaff(false);
        notification.setMessage("Đơn hàng có mã số #"+ orderUser.getOrderId()+ " đã được vận chuyển. Bạn hãy chú ý điện thoại." );
        Notification notificationSave = notificationService.createNotificationOrder(notification);
        simpMessagingTemplate.convertAndSend("/topic/userNotification", notificationSave);

        MessageProcessOrder messageProcessOrder = new MessageProcessOrder();
        messageProcessOrder.setOrderId(orderUser.getOrderId());
        messageProcessOrder.setMessage("Đơn hàng có mã số #"+ orderUser.getOrderId()+ " đã được vận chuyển. Bạn hãy chú ý điện thoại." );
        User user = this.userRepository.findById(orderUser.getUserId()).orElse(null);
        if (user != null) {
            messageProcessOrder.setToEmail(user.getEmail());
            messageProcessOrder.setFullName(user.getFirstName() + " " + user.getLastName());
        }
        kafkaTemplate.send("send-email-order-message", messageProcessOrder);
        return orderUser;
    }

    @Override
    public OrderUser doneOrder(int orderId) {
        OrderUser orderUser = this.orderRepository.findById(orderId).orElse(null);
        if (orderUser != null) {
            if (!orderUser.isDone()) {
                orderUser.setDone(true);
            }
            this.orderRepository.save(orderUser);
        }
        MessageProcessOrder messageProcessOrder = new MessageProcessOrder();
        assert orderUser != null;
        messageProcessOrder.setOrderId(orderUser.getOrderId());
        messageProcessOrder.setMessage("Đơn hàng có mã số #" + orderUser.getOrderId()+ " của bạn đã hoàn thành. Xin cám ơn!");
        User user = this.userRepository.findById(orderUser.getUserId()).orElse(null);
        if (user != null) {
            messageProcessOrder.setToEmail(user.getEmail());
            messageProcessOrder.setFullName(user.getFirstName() + " " + user.getLastName());
        }
        kafkaTemplate.send("send-email-order-message", messageProcessOrder);
        return orderUser;
    }

    @Override
    public List<TotalQuantityProductResponse> findListTotalProductOfMonth(int month) {
        LocalDate startDate = LocalDate.of(2025, month, 1);
        LocalDate endDate = startDate.plusMonths(1);
        return this.productOrderRepository.findListTotalProductOfMonth(startDate, endDate);
    }

    @Override
    public List<TotalQuantityProductResponse> getAllTotalPriceOfYear() {
        return this.productOrderRepository.getAllTotalPriceOfYear();
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
