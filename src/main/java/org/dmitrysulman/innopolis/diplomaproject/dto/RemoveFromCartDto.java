package org.dmitrysulman.innopolis.diplomaproject.dto;

public class RemoveFromCartDto {
    private int productId;
    private boolean completely;

    public RemoveFromCartDto() {
    }

    public RemoveFromCartDto(int productId, boolean completely) {
        this.productId = productId;
        this.completely = completely;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public boolean isCompletely() {
        return completely;
    }

    public void setCompletely(boolean completely) {
        this.completely = completely;
    }
}
