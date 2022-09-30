package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public List<Order> findAll();

    public Optional<Order> findById(int id);

    public void save(OrderDto orderDto, int userId);
}
