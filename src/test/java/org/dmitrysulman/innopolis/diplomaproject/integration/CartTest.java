package org.dmitrysulman.innopolis.diplomaproject.integration;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.services.CartService;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@SpringBootTest
@ActiveProfiles("cart-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartTest {

    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private WebApplicationContext context;

    private static final String testUserEmail = "test@test.ru";
    private static final String testUserPassword = "123";
    private static final String testUserFirstName = "test";
    private static final String testUserSecondName = "test";
    private static final String testUserAddress = "test";
    private static final List<Product> testProducts = new ArrayList<>();
    private static User user;

    @BeforeAll
    void beforeAll() {
        user = createTestUser();

        for (int i = 0; i < 3; i++) {
            testProducts.add(createTestProduct("Product" + i, i * 2000 + 1000, i));
        }
    }

    @BeforeEach
    void setUp() {
        mvc = webAppContextSetup(context)
                .apply(springSecurity())
                .apply(sharedHttpSession())
                .build();
        cartService.clearCart(user.getId());
    }

    User createTestUser() {
        User user = new User();
        user.setEmail("test");
        user.setEmail(testUserEmail);
        user.setPassword(testUserPassword);
        user.setRepeatPassword(testUserPassword);
        user.setFirstName(testUserFirstName);
        user.setSecondName(testUserSecondName);
        user.setAddress(testUserAddress);
        user.setAdmin(false);
        return userService.save(user);
    }

    Product createTestProduct(String name, int price, int amount) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setAmount(amount);
        return productService.save(product);
    }

    @Test
    void should_merge_cart_in_session_with_empty_cart_in_db_after_login() throws Exception {
        mvc
                .perform(get("/"))
                .andExpect(status().isOk());
        mvc
                .perform(post("/cart/add_to_cart")
                        .with(csrf())
                        .content("{\"productId\": " + testProducts.get(0).getId() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc
                .perform(formLogin("/process-signin")
                        .user(testUserEmail)
                        .password(testUserPassword)
                )
                .andExpect(status().is3xxRedirection());

        assertEquals(1, cartService.getCartByUser(user.getId()).getTotalItems());
        assertEquals(1, cartService.getCartByUser(user.getId()).getTotalProducts());
        assertEquals(testProducts.get(0).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(0).getProduct().getId());
        assertEquals(1, cartService.getCartByUser(user.getId()).getCartItems().get(0).getProductAmount());
    }

    @Test
    void should_add_product_to_cart_in_db_for_logged_in_user() throws Exception {
        int productIndex = 0;
        mvc
                .perform(get("/"))
                .andExpect(status().isOk());
        mvc
                .perform(formLogin("/process-signin")
                        .user(testUserEmail)
                        .password(testUserPassword)
                )
                .andExpect(status().is3xxRedirection());
        mvc
                .perform(post("/cart/add_to_cart")
                        .with(csrf())
                        .content("{\"productId\": " + testProducts.get(productIndex).getId() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, cartService.getCartByUser(user.getId()).getTotalItems());
        assertEquals(1, cartService.getCartByUser(user.getId()).getTotalProducts());
        assertEquals(testProducts.get(productIndex).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProduct().getId());
        assertEquals(1, cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProductAmount());

        mvc
                .perform(post("/cart/add_to_cart")
                        .with(csrf())
                        .content("{\"productId\": " + testProducts.get(productIndex).getId() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(2, cartService.getCartByUser(user.getId()).getTotalItems());
        assertEquals(1, cartService.getCartByUser(user.getId()).getTotalProducts());
        assertEquals(testProducts.get(productIndex).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProduct().getId());
        assertEquals(2, cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProductAmount());

        productIndex = 1;
        mvc
                .perform(post("/cart/add_to_cart")
                        .with(csrf())
                        .content("{\"productId\": " + testProducts.get(productIndex).getId() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(3, cartService.getCartByUser(user.getId()).getTotalItems());
        assertEquals(2, cartService.getCartByUser(user.getId()).getTotalProducts());
        assertEquals(testProducts.get(productIndex).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProduct().getId());
        assertEquals(1, cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProductAmount());

        mvc
                .perform(post("/cart/add_to_cart")
                        .with(csrf())
                        .content("{\"productId\": " + testProducts.get(productIndex).getId() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(4, cartService.getCartByUser(user.getId()).getTotalItems());
        assertEquals(2, cartService.getCartByUser(user.getId()).getTotalProducts());
        assertEquals(testProducts.get(productIndex).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProduct().getId());
        assertEquals(2, cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProductAmount());
    }

    @Test
    void should_merge_cart_in_session_with_cart_containing_products_in_db_after_login() throws Exception {
        cartService.addProductToCart(user.getId(), testProducts.get(0).getId());

        int productIndex = 1;
        mvc
                .perform(get("/"))
                .andExpect(status().isOk());
        mvc
                .perform(post("/cart/add_to_cart")
                        .with(csrf())
                        .content("{\"productId\": " + testProducts.get(productIndex).getId() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc
                .perform(formLogin("/process-signin")
                        .user(testUserEmail)
                        .password(testUserPassword)
                )
                .andExpect(status().is3xxRedirection());

        assertEquals(2, cartService.getCartByUser(user.getId()).getTotalItems());
        assertEquals(2, cartService.getCartByUser(user.getId()).getTotalProducts());
        assertEquals(testProducts.get(productIndex).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProduct().getId());
        assertEquals(1, cartService.getCartByUser(user.getId()).getCartItems().get(productIndex).getProductAmount());
    }
}
