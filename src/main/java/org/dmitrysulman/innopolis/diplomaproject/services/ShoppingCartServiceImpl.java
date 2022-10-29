package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
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
    @Transactional
    public void setShoppingCartWithUserAfterLogin(ShoppingCart shoppingCart, int userId) {
        //TODO go to DB
        System.out.println("WE ARE HERE " + shoppingCart.getTotalItems());
    }

    @Override
    public ShoppingCart getShoppingCartByUser(int userId) {
        //TODO go to DB
        return null;
    }

    @Override
    @Transactional
    public void addProductToCart(int userId, int productId) throws ElementNotFoundException {
        //TODO
    }

    @Override
    public void addProductToCart(ShoppingCart shoppingCart, int productId) throws ElementNotFoundException {
        //TODO message
        try {
            shoppingCart
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
                                shoppingCart.getCartItems().add(cartItem);
                            });
        } catch (IllegalArgumentException e) {
            throw new ElementNotFoundException(e.getMessage());
        }
        updateCartContent(shoppingCart);
    }

    @Override
    @Transactional
    public void removeProductFromCart(int userId, int productId, boolean completely) throws ElementNotFoundException {

    }

    @Override
    public void removeProductFromCart(ShoppingCart shoppingCart, int productId, boolean completely) throws ElementNotFoundException {
        //TODO
//        shoppingCart.removeProductFromCart(product.getId());
    }

    @Override
    public void updateCartContent(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().forEach(cartItem -> cartItem.setProduct(productRepository.findById(cartItem.getProduct().getId()).get()));
    }

    @Override
    @Transactional
    public void clearCart(int userId) {
        //TODO
    }
}
