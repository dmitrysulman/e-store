package org.dmitrysulman.innopolis.diplomaproject.repositories;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Product> findByName(String name);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.productImages pi " +
            "WHERE p.id = :id")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    Optional<Product> findByIdWithProductImages(@Param("id") int id);
}
