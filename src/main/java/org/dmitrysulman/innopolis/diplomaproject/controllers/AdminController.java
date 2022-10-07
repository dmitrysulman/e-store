package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.apache.commons.io.FilenameUtils;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.services.ImageService;
import org.dmitrysulman.innopolis.diplomaproject.services.OrderService;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ImageService imageService;

    @Autowired
    public AdminController(UserService userService, OrderService orderService, ProductService productService, ImageService imageService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
        this.imageService = imageService;
    }

    @GetMapping("")
    public String main(Model model) {
        model.addAttribute("users", userService.findAll());

        return "admin/main";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findAll());

        return "admin/orders";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());

        return "admin/products";
    }

    @GetMapping("/add_product")
    public String addProducts(@ModelAttribute("product") Product product) {
        return "admin/add_product";
    }

    @PostMapping("/add_product")
    public String addProducts(@ModelAttribute("product") Product product,
                              BindingResult bindingResult,
                              @RequestParam("image") MultipartFile image) throws IOException {
        if (bindingResult.hasErrors()) {
            return "admin/add_product";
        }
        product = productService.save(product);
        imageService.save(image.getBytes(), product, FilenameUtils.getExtension(image.getOriginalFilename()));

        return "redirect:/admin/products";
    }
}
