package org.dmitrysulman.innopolis.diplomaproject.dto;

import javax.validation.constraints.Min;

//TODO add messages
public class AddToCartDto {
    @Min(value = 1, message = "Order amount should be greater than 0")
    private int productAmount;

    private int productId;

    public AddToCartDto() {
    }

    public AddToCartDto(int productAmount, int productId) {
        this.productAmount = productAmount;
        this.productId = productId;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
