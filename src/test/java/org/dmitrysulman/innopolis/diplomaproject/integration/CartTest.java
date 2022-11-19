package org.dmitrysulman.innopolis.diplomaproject.integration;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.services.CartService;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    void setUp() {
        user = createTestUser();

        for (int i = 0; i < 3; i++) {
           testProducts.add(createTestProduct("Product" + i, i * 2000 + 1000, i));
        }

        mvc = webAppContextSetup(context)
                .apply(springSecurity())
                .apply(sharedHttpSession())
                .build();
    }

    private User createTestUser() {
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

    private Product createTestProduct(String name, int price, int amount) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setAmount(amount);
        return productService.save(product);
    }

    @Test
    void should_merge_the_cart_in_session_with_empty_cart_in_db_after_login() throws Exception {
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

        assertEquals(testProducts.get(0).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(0).getProduct().getId());
        assertEquals(1, cartService.getCartByUser(user.getId()).getCartItems().get(0).getProductAmount());
    }

    @Test
    @Disabled
    void should_merge_the_cart_in_session_with_cart_containing_products_in_db_after_login() throws Exception {
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

        assertEquals(testProducts.get(0).getId(), cartService.getCartByUser(user.getId()).getCartItems().get(0).getProduct().getId());
        assertEquals(1, cartService.getCartByUser(user.getId()).getCartItems().get(0).getProductAmount());
    }
}
