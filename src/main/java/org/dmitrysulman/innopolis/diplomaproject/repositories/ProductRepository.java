package org.dmitrysulman.innopolis.diplomaproject.repositories;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Product> findByName(String name);
}
