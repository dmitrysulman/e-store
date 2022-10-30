package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void setCartWithUserAfterLogin(Cart cart, int userId) {
        //TODO go to DB
        System.out.println("WE ARE HERE " + cart.getTotalItems());
    }

    @Override
    public Cart getCartByUser(int userId) {
        //TODO go to DB
        return null;
    }

    @Override
    @Transactional
    public void addProductToCart(int userId, int productId) throws ElementNotFoundException {
        //TODO
    }

    @Override
    public void addProductToCart(Cart cart, int productId) throws ElementNotFoundException {
        //TODO message
        try {
            cart
                    .getCartItems()
                    .stream()
                    .filter(cartItem -> cartItem.getProduct().getId() == productId)
                    .findFirst()
                    .ifPresentOrElse(cartItem -> cartItem.setProductAmount(cartItem.getProductAmount() + 1),
                            () -> {
                                Product product = productRepository
                                        .findById(productId)
                                        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                                CartItem cartItem = new CartItem();
                                cartItem.setProduct(product);
                                cartItem.setProductAmount(1);
                                cart.getCartItems().add(cartItem);
                            });
            updateCartContent(cart);
        } catch (IllegalArgumentException e) {
            throw new ElementNotFoundException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeProductFromCart(int userId, int productId, boolean completely) throws ElementNotFoundException {
        //TODO
    }

    @Override
    public void removeProductFromCart(Cart cart, int productId, boolean completely) {
        cart
                .getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId() == productId)
                .findFirst()
                .ifPresent(cartItem -> {
                    if (completely || cartItem.getProductAmount() == 1) {
                        cart.getCartItems().removeIf(item -> item.getProduct().getId() == productId);
                        cartItem.setCart(null);
                    } else {
                        cartItem.setProductAmount(cartItem.getProductAmount() - 1);
                    }
                });
        updateCartContent(cart);
    }

    @Override
    public void updateCartContent(Cart cart) {
        cart.getCartItems().forEach(cartItem -> cartItem.setProduct(productRepository.findById(cartItem.getProduct().getId()).orElseThrow(() -> new IllegalArgumentException("Product not found"))));
    }

    @Override
    @Transactional
    public void clearCart(int userId) {
        //TODO
    }
}
