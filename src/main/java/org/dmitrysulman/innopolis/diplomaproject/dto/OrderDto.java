package org.dmitrysulman.innopolis.diplomaproject.dto;

import java.util.List;

public class OrderDto {
    private List<OrderItemDto> products;

    public OrderDto() {
    }

    public OrderDto(List<OrderItemDto> products) {
        this.products = products;
    }

    public List<OrderItemDto> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItemDto> products) {
        this.products = products;
    }
}
