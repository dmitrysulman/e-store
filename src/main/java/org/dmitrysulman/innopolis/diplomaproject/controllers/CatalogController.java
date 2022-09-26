package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogController {

    private final ProductRepository productRepository;

    @Autowired
    public CatalogController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "catalog/index";
    }
}
