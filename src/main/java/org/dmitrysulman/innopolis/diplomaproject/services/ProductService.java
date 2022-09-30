package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductService {
    public Page<Product> findAll(Integer page, Integer perPage, String direction);

    public Optional<Product> findById(int id);

    public Page<Product> findByNameContaining(String name, Integer page, Integer perPage, String direction);
}
