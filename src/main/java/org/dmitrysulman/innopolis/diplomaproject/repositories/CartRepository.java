package org.dmitrysulman.innopolis.diplomaproject.repositories;

import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT DISTINCT c FROM Cart c " +
            "LEFT JOIN FETCH c.cartItems ci " +
            "LEFT JOIN FETCH ci.product p " +
            "WHERE c.user.id = :userId " +
            "ORDER BY p.name")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    Optional<Cart> findByUserIdWithProducts(@Param("userId") int userId);
}
