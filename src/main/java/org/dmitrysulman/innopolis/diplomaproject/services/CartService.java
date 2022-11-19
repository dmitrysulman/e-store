package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;

public interface CartService {
    void mergeCartAfterLogin(Cart cart, int userId);

    Cart getCartByUser(int userId);

    void addProductToCart(Cart cart, int productId) throws ElementNotFoundException;

    void addProductToCart(int userId, int productId) throws ElementNotFoundException;

    void removeProductFromCart(Cart cart, int productId, boolean completely);

    void removeProductFromCart(int userId, int productId, boolean completely) throws ElementNotFoundException;

    void clearCart(Cart cart);

    void clearCart(int userId);

    void updateCartContent(Cart cart);
}