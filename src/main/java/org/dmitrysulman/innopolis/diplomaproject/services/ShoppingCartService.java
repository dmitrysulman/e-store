package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.AddToCartDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;

public interface ShoppingCartService {
    void setShoppingCartWithUserAfterLogin(ShoppingCart shoppingCart, int userId);

    ShoppingCart getShoppingCartByUser(int userId);

    void addProductToCart(ShoppingCart shoppingCart, int productId) throws ElementNotFoundException;

    void addProductToCart(int userId, int productId) throws ElementNotFoundException;

    void removeProductFromCart(ShoppingCart shoppingCart, int productId, boolean completely) throws ElementNotFoundException;

    void removeProductFromCart(int userId, int productId, boolean completely) throws ElementNotFoundException;

    void updateCartContent(ShoppingCart shoppingCart);

    void clearCart(int userId);
}