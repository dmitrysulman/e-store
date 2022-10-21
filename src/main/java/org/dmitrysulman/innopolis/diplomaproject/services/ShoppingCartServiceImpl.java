package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.AddToCartDto;
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
        System.out.println("WE ARE HERE " + shoppingCart.getTotalItems());
    }

    @Override
    public ShoppingCart getShoppingCartByUser(User user) {
        //TODO go to DB
        return null;
    }

    @Override
    public void addProductToCartOrChangeCount(ShoppingCart shoppingCart, AddToCartDto addToCartDto, User user) {
        shoppingCart
                .getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId() == addToCartDto.getProductId())
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setCount(cartItem.getCount() + addToCartDto.getProductsAmount()),
                        () -> {
                            Product product = productRepository.findById(addToCartDto.getProductId()).orElse(null);
                            CartItem cartItem = new CartItem();
                            cartItem.setProduct(product);
                            cartItem.setCount(addToCartDto.getProductsAmount());
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
