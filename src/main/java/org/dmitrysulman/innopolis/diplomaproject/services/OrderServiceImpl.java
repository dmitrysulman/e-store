package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Order;
import org.dmitrysulman.innopolis.diplomaproject.models.OrderStatus;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.OrderRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    public void save(OrderDto orderDto, int userId) {
        Order order = new Order();
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(orderDto.getProductId()).orElse(null);
        int orderAmount = product.getPrice() * orderDto.getProductsAmount();
        order.setUser(user);
        order.setProduct(product);
        order.setProductsAmount(orderDto.getProductsAmount());
        order.setOrderAmount(orderAmount);
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.NEW);
        orderRepository.save(order);
    }
}
