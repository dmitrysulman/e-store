package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.CartRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository,
                           CartRepository cartRepository,
                           @Lazy CartService cartService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public void mergeCartAfterLogin(Cart cart, int userId) {
        if (!cart.getCartItems().isEmpty()) {
            Cart cartFromDb = cartRepository.findById(userId).orElseThrow(IllegalStateException::new);
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
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCartByUser(int userId) {
        return cartRepository.findById(userId).orElseThrow(IllegalStateException::new);
    }

    @Override
    @Transactional
    public void addProductToCart(int userId, int productId) throws ElementNotFoundException {
        Cart cart = cartRepository.findById(userId).orElseThrow(IllegalStateException::new);
        addProductToCartInternal(cart, productId, true);
        cartRepository.save(cart);
    }

    @Override
    public void addProductToCart(Cart cart, int productId) throws ElementNotFoundException {
        addProductToCartInternal(cart, productId, false);
        cartService.updateCartContent(cart);
    }

    private void addProductToCartInternal(Cart cart, int productId, boolean fromDb) throws ElementNotFoundException {
        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId() == productId)
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setProductAmount(cartItem.getProductAmount() + 1),
                        () -> cart.getCartItems().add(new CartItem(getProductById(productId, fromDb), 1, cart)));
    }

    private Product getProductById(int productId, boolean fromDb) {
        Product product;
        if (fromDb) {
            product = productRepository.getReferenceById(productId);
        } else {
            product = new Product();
            product.setId(productId);
        }
        return product;
    }

    @Override
    @Transactional
    public void removeProductFromCart(int userId, int productId, boolean completely) throws ElementNotFoundException {
        Cart cart = cartRepository.findById(userId).orElseThrow(IllegalStateException::new);
        removeProductFromCartInternal(cart, productId, completely);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(Cart cart) {
        clearCartInternal(cart);
    }

    @Override
    public void removeProductFromCart(Cart cart, int productId, boolean completely) {
        removeProductFromCartInternal(cart, productId, completely);
        cartService.updateCartContent(cart);
    }

    private void removeProductFromCartInternal(Cart cart, int productId, boolean completely) {
        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId() == productId)
                .findFirst()
                .ifPresent(cartItem -> {
                    if (completely || cartItem.getProductAmount() == 1) {
                        cart.getCartItems().removeIf(item -> item.getProduct().getId() == productId);
                    } else {
                        cartItem.setProductAmount(cartItem.getProductAmount() - 1);
                    }
                });
    }

    @Override
    @Transactional(readOnly = true)
    public void updateCartContent(Cart cart) {
        Iterator<CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            Optional<Product> product = productRepository.findById(cartItem.getProduct().getId());
            product.ifPresentOrElse(cartItem::setProduct, iterator::remove);
        }
    }

    @Override
    @Transactional
    public void clearCart(int userId) {
        Cart cart = cartRepository.findById(userId).orElseThrow(IllegalStateException::new);
        clearCartInternal(cart);
        cartRepository.save(cart);
    }

    private void clearCartInternal(Cart cart) {
        cart.getCartItems().clear();
    }
}
