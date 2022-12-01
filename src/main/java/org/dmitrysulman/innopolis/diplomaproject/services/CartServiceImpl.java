package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.repositories.CartItemRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.CartRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductRepository;
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
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository,
                           CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           @Lazy CartService cartService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public void mergeCartAfterLogin(Cart cartSession, int userId) {
        if (!cartSession.getCartItems().isEmpty()) {
            Cart cartDb = cartRepository.findById(userId).orElseThrow(IllegalStateException::new);
            for (CartItem cartItemSession : cartSession.getCartItems()) {
                Optional<CartItem> cartItemDb = findCartItemByCartIdAndProductId(cartDb,
                        cartItemSession.getProduct().getId());
                if (cartItemDb.isEmpty()) {
                    cartItemSession.setCart(cartDb);
                    cartItemRepository.save(cartItemSession);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCartByUser(int userId) {
        return cartRepository.findByUserIdWithProducts(userId).orElseThrow(IllegalStateException::new);
    }

    @Override
    @Transactional
    public void addProductToCart(int userId, int productId) {
        Optional<CartItem> cartItem = findCartItemByCartIdAndProductId(userId, productId);
        cartItem.ifPresentOrElse(item -> item.setProductAmount(item.getProductAmount() + 1),
                () -> {
                    CartItem item = new CartItem(productRepository.getReferenceById(productId), 1, cartRepository.getReferenceById(userId));
                    cartItemRepository.save(item);
                });
    }

    @Override
    public void addProductToCart(Cart cart, int productId) {
        Optional<CartItem> cartItem = findCartItemByCartIdAndProductId(cart, productId);
        cartItem.ifPresentOrElse(item -> item.setProductAmount(item.getProductAmount() + 1),
                () -> {
                    Product product = new Product();
                    product.setId(productId);
                    cart.getCartItems().add(new CartItem(product, 1, cart));
                });
        cartService.updateCartContent(cart);
    }

    @Override
    @Transactional
    public void removeProductFromCart(int userId, int productId, boolean completely) {
        Optional<CartItem> cartItem = findCartItemByCartIdAndProductId(userId, productId);
        cartItem.ifPresent(item -> {
            if (completely || item.getProductAmount() == 1) {
                cartItemRepository.delete(item);
            } else {
                item.setProductAmount(item.getProductAmount() - 1);
            }
        });
    }

    @Override
    public void removeProductFromCart(Cart cart, int productId, boolean completely) {
        Optional<CartItem> cartItem = findCartItemByCartIdAndProductId(cart, productId);
        cartItem.ifPresent(item -> {
            if (completely || item.getProductAmount() == 1) {
                cart.getCartItems().removeIf(it -> it.getProduct().getId() == productId);
            } else {
                item.setProductAmount(item.getProductAmount() - 1);
            }
        });
        cartService.updateCartContent(cart);
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
    public void clearCart(Cart cart) {
        cart.getCartItems().clear();
    }

    @Override
    @Transactional
    public void clearCart(int userId) {
        cartItemRepository.deleteByCartId(userId);
    }

    @Override
    public Optional<CartItem> findCartItemByCartIdAndProductId(Cart cart, int productId) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId() == productId)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartItem> findCartItemByCartIdAndProductId(int userId, int productId) {
        return cartItemRepository.findByCartIdAndProductId(userId, productId);
    }
}
