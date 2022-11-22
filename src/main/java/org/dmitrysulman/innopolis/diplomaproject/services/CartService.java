package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;

import java.util.Optional;

public interface CartService {
    void mergeCartAfterLogin(Cart cart, int userId);

    Cart getCartByUser(int userId);

    void addProductToCart(Cart cart, int productId);

    void addProductToCart(int userId, int productId);

    void removeProductFromCart(Cart cart, int productId, boolean completely);

    void removeProductFromCart(int userId, int productId, boolean completely);

    void clearCart(Cart cart);

    void clearCart(int userId);

    void updateCartContent(Cart cart);

    Optional<CartItem> findCartItemByCartIdAndProductId(Cart cart, int productId);

    Optional<CartItem> findCartItemByCartIdAndProductId(int userId, int productId);
}