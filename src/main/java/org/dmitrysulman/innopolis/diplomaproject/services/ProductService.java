package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    Page<Product> findAll(Integer page, Integer perPage, String direction);

    Optional<Product> findById(int id);

    Optional<Product> findByIdWithImagesUrls(int id);

    Optional<Product> findByName(String name);

    Page<Product> findByNameContaining(String name, Integer page, Integer perPage, String direction);

    Product save(Product product);
}
