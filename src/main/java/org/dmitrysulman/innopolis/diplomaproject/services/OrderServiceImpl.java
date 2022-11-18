package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.OrderItemDto;
import org.dmitrysulman.innopolis.diplomaproject.models.*;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.OrderRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.UserRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
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
    public Order save(OrderDto orderDto, int userId) throws ElementNotFoundException {
        try {
            Order order = new Order();
            User user = userRepository.getReferenceById(userId);
            order.setUser(user);
            order.setOrderDate(Instant.now());
            order.setOrderStatus(OrderStatus.NEW);
            List<OrderProduct> orderProducts = new ArrayList<>();
            for (OrderItemDto orderItemDto : orderDto.getOrderProducts()) {
                Product product = productRepository.findById(orderItemDto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
                OrderProduct orderProduct = new OrderProduct(order, product, product.getPrice(), orderItemDto.getProductAmount());
                orderProducts.add(orderProduct);
                product.setAmount(product.getAmount() - orderItemDto.getProductAmount());
            }
            order.setOrderProducts(orderProducts);
            return orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new ElementNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Order> findByUserId(int userId) {
        return orderRepository.findByUserIdWithProducts(userId);
    }
}
