package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.*;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.OrderRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order save(OrderDto orderDto, int userId) {
        Order order = new Order();
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(orderDto.getProductId()).orElse(null);
//        int orderAmount = product.getPrice() * orderDto.getProductsAmount();
        OrderProduct orderProduct = new OrderProduct(order, product, product.getPrice(), orderDto.getProductsAmount());
//        orderProduct.setProduct(product);
//        orderProduct.setProductAmount(orderDto.getProductsAmount());
//        orderProduct.setProductPrice(product.getPrice());
//        orderProduct.setOrder(order);
        order.setUser(user);
        order.setOrderProducts(Collections.singletonList(orderProduct));
//        order.setProduct(product);
//        order.setProductsAmount(orderDto.getProductsAmount());
//        order.setOrderAmount(orderAmount);
        order.setOrderDate(Instant.now());
        order.setOrderStatus(OrderStatus.NEW);
        order.setProductsAmount(orderDto.getProductsAmount());
        order.setOrderAmount(product.getPrice() * orderDto.getProductsAmount());
//        orderProductRepository.save(orderProduct);
        order = orderRepository.save(order);
        product.setAmount(product.getAmount() - orderDto.getProductsAmount());
        productRepository.save(product);
        return order;
    }
}
