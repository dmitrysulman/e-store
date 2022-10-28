package org.dmitrysulman.innopolis.diplomaproject.dto;

import javax.validation.Valid;
import java.util.List;

public class OrderDto {
    @Valid
    private List<OrderItemDto> orderProducts;

    public OrderDto() {
    }

    public OrderDto(List<OrderItemDto> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public List<OrderItemDto> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderItemDto> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
