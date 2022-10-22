package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.AddToCartDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.dmitrysulman.innopolis.diplomaproject.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;
    private final MessageSource messageSource;

    @Autowired
    public ProductController(ProductService productService,
                             ShoppingCartService shoppingCartService,
                             MessageSource messageSource) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       @ModelAttribute("addToCartDto") AddToCartDto addToCartDto,
                       Model model) {
        Product product = productService.findByIdWithImagesUrls(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                messageSource.getMessage(
                                        "errors.productcontroller.productnotfound",
                                        null,
                                        LocaleContextHolder.getLocale()
                                )
                        )
                );
        addToCartDto.setProductAmount(1);
        addToCartDto.setProductId(product.getId());
        model.addAttribute("product", product);

        return "product/show";
    }

    @PostMapping("/add_to_cart")
    public String addToCart(@ModelAttribute("addToCartDto") @Valid AddToCartDto addToCartDto,
                            BindingResult bindingResult,
                            Model model,
                            HttpSession httpSession,
                            Authentication authentication) {
        if (bindingResult.hasErrors()) {
            Product product = productService.findById(addToCartDto.getProductId())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    messageSource.getMessage(
                                            "errors.productcontroller.productnotfound",
                                            null,
                                            LocaleContextHolder.getLocale()
                                    )
                            )
                    );
            model.addAttribute("product", product);
            return "product/show";
        }
        User user = null;
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            user = userDetails.getUser();
        }
        shoppingCartService.addProductToCartOrChangeCount((ShoppingCart) httpSession.getAttribute("cart"), addToCartDto, user);

        return "redirect:/product/" + addToCartDto.getProductId();
    }
}
