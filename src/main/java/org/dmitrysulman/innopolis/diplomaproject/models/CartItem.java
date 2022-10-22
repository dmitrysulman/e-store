package org.dmitrysulman.innopolis.diplomaproject.models;

public class CartItem {
    private Product product;
    private int productAmount;
    private ShoppingCart shoppingCart;

    public CartItem() {
    }

    public CartItem(Product product, int productAmount, ShoppingCart shoppingCart) {
        this.product = product;
        this.productAmount = productAmount;
        this.shoppingCart = shoppingCart;
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

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public int getTotalPrice() {
        return product.getPrice() * productAmount;
    }
}
