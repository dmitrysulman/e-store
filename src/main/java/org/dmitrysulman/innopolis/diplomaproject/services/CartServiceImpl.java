package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.CartRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.UserRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository,
                           CartRepository cartRepository,
                           UserRepository userRepository,
                           @Lazy CartService cartService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public void mergeCartAfterLogin(Cart cart, int userId) {
        Optional<Cart> cartFromDbOptional = cartRepository.findByUserId(userId);
        if (cartFromDbOptional.isPresent()) {
            Cart cartFromDb = cartFromDbOptional.get();
            for (CartItem cartItemSession : cart.getCartItems()) {
                boolean productNotPresentInDb = cartFromDb.getCartItems().stream()
                        .filter(c -> c.getProduct().getId() == cartItemSession.getProduct().getId())
                        .findFirst()
                        .isEmpty();
                if (productNotPresentInDb) {
                    cartFromDb.getCartItems().add(cartItemSession);
                    cartItemSession.setCart(cartFromDb);
                    cartRepository.save(cartFromDb);
                }
            }
        } else {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            for (CartItem cartItem : cart.getCartItems()) {
                Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
                cartItem.setProduct(product);
            }
            cart.setUser(user);
            cartRepository.save(cart);
        }
    }

    @Override
    public Cart getCartByUser(int userId) {
        return cartRepository.findByUserId(userId).orElse(null);
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
            cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId() == productId)
                    .findFirst()
                    .ifPresentOrElse(cartItem -> cartItem.setProductAmount(cartItem.getProductAmount() + 1),
                            () -> {
                                Product product = productRepository
                                        .findById(productId)
                                        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                                CartItem cartItem = new CartItem(product, 1, cart);
                                cart.getCartItems().add(cartItem);
                            });
            cartService.updateCartContent(cart);
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
        cart.getCartItems().stream()
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
        cartService.updateCartContent(cart);
    }

    @Override
    public void updateCartContent(Cart cart) {
        cart.getCartItems().forEach(
                cartItem -> cartItem.setProduct(
                        productRepository.findById(cartItem.getProduct().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Product not found"))
                )
        );
    }

    @Override
    @Transactional
    public void clearCart(int userId) {
        //TODO
    }
}
