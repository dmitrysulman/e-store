package org.dmitrysulman.innopolis.diplomaproject.dto;

import javax.validation.constraints.Min;

//TODO add messages
public class OrderItemDto {
    @Min(value = 1, message = "Order amount should be greater than 0")
    private Integer productAmount;

    private Integer productId;

    public OrderItemDto(Integer productAmount, Integer productId) {
        this.productAmount = productAmount;
        this.productId = productId;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
