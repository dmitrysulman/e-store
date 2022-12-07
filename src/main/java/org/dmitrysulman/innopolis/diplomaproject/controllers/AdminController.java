package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.apache.commons.io.FilenameUtils;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.services.ImageService;
import org.dmitrysulman.innopolis.diplomaproject.services.OrderService;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.dmitrysulman.innopolis.diplomaproject.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ImageService imageService;
    private final ProductValidator productValidator;

    @Autowired
    public AdminController(UserService userService,
                           OrderService orderService,
                           ProductService productService,
                           ImageService imageService,
                           ProductValidator productValidator) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
        this.imageService = imageService;
        this.productValidator = productValidator;
    }

    @InitBinder("product")
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmer);
        binder.addValidators(productValidator);
    }

    @GetMapping("")
    public String main(Model model) {
        model.addAttribute("users", userService.findAll());

        return "admin/main";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "per_page", required = false) Integer perPage,
                         @RequestParam(value = "direction", required = false) String direction,
                         Model model) {
        model.addAttribute("orders", orderService.findAll(page, perPage, direction));

        return "admin/orders";
    }

    @GetMapping("/products")
    public String products(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "per_page", required = false) Integer perPage,
                           @RequestParam(value = "direction", required = false) String direction,
                           Model model) {
        model.addAttribute("query", "");
        model.addAttribute("products", productService.findAll(page, perPage, direction));

        return "admin/products";
    }

    @GetMapping("/products/search")
    public String productsSearch(@RequestParam(value = "query", required = false) String query,
                                 @RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "per_page", required = false) Integer perPage,
                                 @RequestParam(value = "direction", required = false) String direction,
                                 Model model) {
        if (query == null || query.equals("")) {
            return "redirect:/";
        }
        model.addAttribute("query", query);
        model.addAttribute("products", productService.findByNameContaining(query, page, perPage, direction));

        return "admin/products";
    }

    @GetMapping("/add_product")
    public String addProducts(@ModelAttribute("product") Product product) {
        return "admin/add_product";
    }

    @PostMapping("/add_product")
    public String addProducts(@ModelAttribute("product") @Valid Product product,
                              BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "admin/add_product";
        }
        MultipartFile[] images = product.getImages();
        product = productService.save(product);
        for (MultipartFile image : images) {
            if (image.getBytes().length != 0) {
                imageService.save(image.getBytes(), product, FilenameUtils.getExtension(image.getOriginalFilename()));
            }
        }

        return "redirect:/admin/products";
    }
}
