package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogController {
    private final ProductService productService;

    @Autowired
    public CatalogController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "per_page", required = false) Integer perPage,
                        @RequestParam(value = "direction", required = false) String direction,
                        Model model) {
        model.addAttribute("query", "");
        model.addAttribute("products", productService.findAll(page, perPage, direction));

        return "catalog/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query,
                         @RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "per_page", required = false) Integer perPage,
                         @RequestParam(value = "direction", required = false) String direction,
                         Model model) {
        if (query == null || query.equals("")) {
                return "redirect:/";
        }
        model.addAttribute("query", query);
        model.addAttribute("products", productService.findByNameContaining(query, page, perPage, direction));

        return "/catalog/index";
    }
}
