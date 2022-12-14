package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Order;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();

    Page<Order> findAll(Integer page, Integer perPage, String direction);

    Optional<Order> findById(int id);

    Order save(OrderDto orderDto, int userId) throws ElementNotFoundException;

    List<Order> findByUserId(int userId);
}
