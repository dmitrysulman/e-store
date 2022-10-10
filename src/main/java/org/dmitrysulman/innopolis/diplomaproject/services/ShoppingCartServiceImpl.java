package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ProductRepository productRepository;

    @Autowired
    public ShoppingCartServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ShoppingCart getShoppingCart() {
        return new ShoppingCart();
    }

    @Override
    public void addProductToCartOrChangeCount(ShoppingCart shoppingCart, OrderDto orderDto) {
        Product product = productRepository.findById(orderDto.getProductId()).orElse(null);
        shoppingCart.addProductToCartOrChangeCount(product, orderDto.getProductsAmount());
    }

    @Override
    public void removeProductFromCart(ShoppingCart shoppingCart, Product product) {
        shoppingCart.removeProductFromCart(product);
    }

}
