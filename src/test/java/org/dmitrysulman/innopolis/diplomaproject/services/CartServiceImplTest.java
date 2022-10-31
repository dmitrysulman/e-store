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

import java.util.ArrayList;
import java.util.List;
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
        int productId = 1;
        int productAmount = 10;
        Cart cart = prepareCartWithOneProduct(productId, productAmount);
        cartService.removeProductFromCart(cart, productId, false);
        CartItem cartItem = cart.getCartItems().get(0);
        assertEquals(productAmount - 1, cartItem.getProductAmount());
    }

    @Test
    void removeProductFromCartWithAmount1ShouldCompletelyRemoveProduct() {
        int productId = 1;
        int productAmount = 1;
        Cart cart = prepareCartWithOneProduct(productId, productAmount);
        cartService.removeProductFromCart(cart, productId, false);
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void removeProductFromCartWithCompletelyTrueShouldCompletelyRemoveProduct() {
        int productId = 1;
        int productAmount = 10;
        Cart cart = prepareCartWithOneProduct(productId, productAmount);
        cartService.removeProductFromCart(cart, productId, true);
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void removeProductFromCartThatNotInCartShouldDoNothing() {
        int productId = 1;
        int notValidProductId = 2;
        int productAmount = 10;
        Cart cart = prepareCartWithOneProduct(productId, productAmount);
        assertDoesNotThrow(() -> cartService.removeProductFromCart(cart, notValidProductId, true));
    }

    private Cart prepareCartWithOneProduct(int productId, int productAmount) {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId(productId);
        CartItem cartItem = new CartItem(product, productAmount, cart);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));
        Mockito.lenient().when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        return cart;
    }
}