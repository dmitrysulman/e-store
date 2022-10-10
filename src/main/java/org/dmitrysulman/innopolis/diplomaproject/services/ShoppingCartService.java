package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCart();

    void addProductToCartOrChangeCount(ShoppingCart shoppingCart, OrderDto orderDto);

    void removeProductFromCart(ShoppingCart shoppingCart, Product product);

}