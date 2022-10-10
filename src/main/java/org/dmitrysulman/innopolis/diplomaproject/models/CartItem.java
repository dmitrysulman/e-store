package org.dmitrysulman.innopolis.diplomaproject.models;

public class CartItem {
    private Product product;
    private int count;
    private ShoppingCart shoppingCart;

    public CartItem() {
    }

    public CartItem(Product product, int count, ShoppingCart shoppingCart) {
        this.product = product;
        this.count = count;
        this.shoppingCart = shoppingCart;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
}
