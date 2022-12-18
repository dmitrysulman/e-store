package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.OrderItemDto;
import org.dmitrysulman.innopolis.diplomaproject.models.*;
import org.dmitrysulman.innopolis.diplomaproject.repositories.OrderRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.UserRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.dmitrysulman.innopolis.diplomaproject.util.PageableHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PageableHelper pageableHelper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository,
                            PageableHelper pageableHelper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pageableHelper = pageableHelper;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> findAll(Integer page, Integer perPage, String direction) {
        Pageable pageable = pageableHelper.preparePageable(page, perPage, direction, "id");
        Page<Integer> ids = orderRepository.findAllOrderIds(pageable);
        List<Order> orders = orderRepository.findAllWithProductsAndUser(ids.toList());
        return ids.map(id -> orders.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .get()
        );
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
