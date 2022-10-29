package org.dmitrysulman.innopolis.diplomaproject.models;

public class CartItem {
    private Product product;
    private int productAmount;
    private Cart cart;

    public CartItem() {
    }

    public CartItem(Product product, int productAmount, Cart cart) {
        this.product = product;
        this.productAmount = productAmount;
        this.cart = cart;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getTotalPrice() {
        return product.getPrice() * productAmount;
    }
}
