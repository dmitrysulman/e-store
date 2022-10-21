package org.dmitrysulman.innopolis.diplomaproject.dto;

import javax.validation.constraints.Min;

//TODO add messages
public class OrderItemDto {
    @Min(value = 1, message = "Order amount should be greater than 0")
    private Integer productsAmount;

    private Integer productId;

    public OrderItemDto(Integer productsAmount, Integer productId) {
        this.productsAmount = productsAmount;
        this.productId = productId;
    }

    public Integer getProductsAmount() {
        return productsAmount;
    }

    public void setProductsAmount(Integer productsAmount) {
        this.productsAmount = productsAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
