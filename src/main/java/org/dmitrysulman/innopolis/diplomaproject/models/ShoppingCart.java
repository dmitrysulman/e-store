package org.dmitrysulman.innopolis.diplomaproject.models;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private User user;
    private List<CartItem> cartItems;

    public ShoppingCart() {
        cartItems = new ArrayList<>();
    }

    public ShoppingCart(User user, List<CartItem> cartItems) {
        this.user = user;
        this.cartItems = cartItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getTotalProducts() {
        return cartItems.size();
    }

    public int getTotalItems() {
        return cartItems.stream()
                .mapToInt(CartItem::getProductAmount)
                .reduce(0, Integer::sum);
    }

    public int getTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .reduce(0, Integer::sum);
    }
}
