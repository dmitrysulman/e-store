package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    @Autowired
    public ShoppingCartServiceImpl(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @Override
    public ShoppingCart getShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new ArrayList<>());
        return shoppingCart;
    }

    @Override
    public ShoppingCart getShoppingCart(User user) {
        //TODO go to DB
        getShoppingCart();
        return null;
    }

    @Override
    public void addProductToCartOrChangeCount(ShoppingCart shoppingCart, OrderDto orderDto) {
        shoppingCart
                .getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId() == orderDto.getProductId())
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setCount(cartItem.getCount() + orderDto.getProductsAmount()),
                        () -> {
                            Product product = productRepository.findById(orderDto.getProductId()).orElse(null);
                            CartItem cartItem = new CartItem();
                            cartItem.setProduct(product);
                            cartItem.setCount(orderDto.getProductsAmount());
                            shoppingCart.getCartItems().add(cartItem);
                        });
        updateCartContent(shoppingCart);
    }

    @Override
    public void removeProductFromCart(ShoppingCart shoppingCart, Product product) {
//        shoppingCart.removeProductFromCart(product.getId());
    }

    @Override
    public void updateCartContent(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().forEach(cartItem -> entityManager.refresh(entityManager.merge(cartItem.getProduct())));
    }
}
