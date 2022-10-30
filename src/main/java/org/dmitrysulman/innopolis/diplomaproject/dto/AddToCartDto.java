package org.dmitrysulman.innopolis.diplomaproject.dto;

//TODO add messages
public class AddToCartDto {
    private int productId;

    public AddToCartDto() {
    }

    public AddToCartDto(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
