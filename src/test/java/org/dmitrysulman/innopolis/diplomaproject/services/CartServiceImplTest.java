package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.CartItem;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void remove_product_from_cart_should_decrease_product_amount_by_one() {
        Map<Integer, Integer> products = new HashMap<>();
        int productId = 1;
        int productAmount = 10;
        products.put(productId, productAmount);
        Cart cart = prepareCart(products);
        cartService.removeProductFromCart(cart, productId, false);
        CartItem cartItem = cart.getCartItems().get(0);
        assertEquals(productAmount - 1, cartItem.getProductAmount());
    }

    @Test
    void remove_product_from_cart_with_amount_1_should_completely_remove_product() {
        Map<Integer, Integer> products = new HashMap<>();
        int productId = 1;
        int productAmount = 1;
        products.put(productId, productAmount);
        Cart cart = prepareCart(products);
        cartService.removeProductFromCart(cart, productId, false);
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void remove_product_from_cart_with_completely_true_should_completely_remove_product() {
        Map<Integer, Integer> products = new HashMap<>();
        int productId = 1;
        int productAmount = 10;
        products.put(productId, productAmount);
        Cart cart = prepareCart(products);
        cartService.removeProductFromCart(cart, productId, true);
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void remove_product_from_cart_that_not_in_cart_should_do_nothing() {
        Map<Integer, Integer> products = new HashMap<>();
        int productId = 1;
        int productAmount = 10;
        products.put(productId, productAmount);
        Cart cart = prepareCart(products);
        int notValidProductId = 2;
        assertDoesNotThrow(() -> cartService.removeProductFromCart(cart, notValidProductId, true));
    }

    @Test
    void add_product_to_empty_cart_should_add_product() {
        int productId = 1;
        Cart cart = new Cart();
        Product product = new Product();
        product.setId(productId);
        mockRepository(productId, product);
        try {
            cartService.addProductToCart(cart, productId);
        } catch (ElementNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, cart.getCartItems().size());
        assertEquals(1, cart.getTotalItems());
        assertEquals(1, cart.getCartItems().get(0).getProductAmount());
        assertEquals(productId, cart.getCartItems().get(0).getProduct().getId());
    }

    @Test
    void add_product_to_cart_containing_this_product_should_increase_product_amount_by_1() {
        Map<Integer, Integer> products = new HashMap<>();
        int productId = 1;
        int productAmount = 1;
        products.put(productId, productAmount);
        Cart cart = prepareCart(products);
        try {
            cartService.addProductToCart(cart, productId);
        } catch (ElementNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, cart.getCartItems().size());
        assertEquals(productAmount + 1, cart.getTotalItems());
        assertEquals(productId, cart.getCartItems().get(0).getProduct().getId());
    }

    @Test
    void add_product_to_cart_containing_other_product_should_add_this_product() {
        Map<Integer, Integer> products = new HashMap<>();
        int otherProductId = 1;
        int otherProductAmount = 2;
        products.put(otherProductId, otherProductAmount);
        Cart cart = prepareCart(products);
        int thisProductId = 2;
        Product thisProduct = new Product();
        thisProduct.setId(thisProductId);
        mockRepository(thisProductId, thisProduct);
        try {
            cartService.addProductToCart(cart, thisProductId);
        } catch (ElementNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, cart.getCartItems().size());
        assertEquals(otherProductAmount + 1, cart.getTotalItems());
        assertTrue(cart.getCartItems().stream().anyMatch(cartItem -> cartItem.getProduct().getId() == thisProductId));
    }

    @Test
    void add_product_to_cart_containing_this_product_and_other_product_should_increase_this_product_amount_by_1() {
        Map<Integer, Integer> products = new HashMap<>();
        int otherProductId = 1;
        int otherProductAmount = 2;
        int thisProductId = 2;
        int thisProductAmount = 3;
        products.put(otherProductId, otherProductAmount);
        products.put(thisProductId, thisProductAmount);
        Cart cart = prepareCart(products);
        try {
            cartService.addProductToCart(cart, thisProductId);
        } catch (ElementNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, cart.getCartItems().size());
        assertEquals(thisProductAmount + otherProductAmount + 1, cart.getTotalItems());
        assertTrue(cart.getCartItems().stream().anyMatch(cartItem -> cartItem.getProduct().getId() == thisProductId));
    }

    private Cart prepareCart(Map<Integer, Integer> products) {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        for (Integer productId : products.keySet()) {
            int productAmount = products.get(productId);
            Product product = new Product();
            product.setId(productId);
            CartItem cartItem = new CartItem(product, productAmount, cart);
            cartItems.add(cartItem);
            mockRepository(productId, product);
        }
        cart.setCartItems(cartItems);
        return cart;
    }

    private void mockRepository(int productId, Product product) {
        lenient().when(productRepository.findById(productId)).thenReturn(Optional.of(product));
    }
}