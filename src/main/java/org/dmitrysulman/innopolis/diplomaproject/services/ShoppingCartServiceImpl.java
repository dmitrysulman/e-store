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

@Service
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ProductRepository productRepository;

    @Autowired
    public ShoppingCartServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void setShoppingCartWithUserAfterLogin(User user, ShoppingCart shoppingCart) {
        //TODO go to DB
    }

    @Override
    public ShoppingCart getShoppingCartByUser(User user) {
        //TODO go to DB
        return null;
    }

    @Override
    public void addProductToCartOrChangeCount(ShoppingCart shoppingCart, OrderDto orderDto, User user) {
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
        shoppingCart.getCartItems().forEach(cartItem -> cartItem.setProduct(productRepository.findById(cartItem.getProduct().getId()).get()));
    }
}
