package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void removeProductFromCartShouldDecreaseProductAmountByOne() {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId(1);
        CartItem cartItem = new CartItem(product, 10, cart);
        cart.setCartItems(Collections.singletonList(cartItem));
        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        cartService.removeProductFromCart(cart, 1, false);
        assertEquals(9, cartItem.getProductAmount());
    }

    @Test
    void removeProductFromCartWithAmount1ShouldCompletelyRemoveProduct() {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId(1);
        CartItem cartItem = new CartItem(product, 1, cart);
        cart.setCartItems(Collections.singletonList(cartItem));
        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        cartService.removeProductFromCart(cart, 1, false);
        assertTrue(cart.getCartItems().isEmpty());
    }
}