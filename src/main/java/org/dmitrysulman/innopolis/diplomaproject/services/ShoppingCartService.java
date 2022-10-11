package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;

public interface ShoppingCartService {
    void setShoppingCartWithUserAfterLogin(User user, ShoppingCart shoppingCart);

    ShoppingCart getShoppingCartByUser(User user);

    void addProductToCartOrChangeCount(ShoppingCart shoppingCart, OrderDto orderDto, User user);

    void removeProductFromCart(ShoppingCart shoppingCart, Product product);

    void updateCartContent(ShoppingCart shoppingCart);
}