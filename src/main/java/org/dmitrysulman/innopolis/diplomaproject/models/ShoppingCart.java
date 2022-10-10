package org.dmitrysulman.innopolis.diplomaproject.models;

import java.util.HashMap;
import java.util.Map;

public final class ShoppingCart {
    private final Map<Product, Integer> shoppingCart;

    public ShoppingCart() {
        shoppingCart = new HashMap<>();
    }

    public void addProductToCartOrChangeCount(Product product, Integer count) {
        shoppingCart.put(product, count);
    }

    public void removeProductFromCart(Product product) {
        shoppingCart.remove(product);
    }

    public Map<Product, Integer> shoppingCart() {
        return new HashMap<>(shoppingCart);
    }

    public int getTotalProducts() {
        return shoppingCart.size();
    }

    public int getTotalItems() {
        return shoppingCart.values().stream().reduce(0, Integer::sum);
    }
}
