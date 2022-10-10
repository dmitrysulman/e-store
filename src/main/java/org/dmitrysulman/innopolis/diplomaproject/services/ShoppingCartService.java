package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;

public interface ShoppingCartService {
    ShoppingCart getShoppingCart();

    ShoppingCart getShoppingCart(User user);

    void addProductToCartOrChangeCount(ShoppingCart shoppingCart, OrderDto orderDto);

    void removeProductFromCart(ShoppingCart shoppingCart, Product product);

    void updateCartContent(ShoppingCart shoppingCart);
}