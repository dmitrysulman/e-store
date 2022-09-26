package org.dmitrysulman.innopolis.diplomaproject.repositiries;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
