package org.dmitrysulman.innopolis.diplomaproject.repositories;

import org.dmitrysulman.innopolis.diplomaproject.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderProducts op " +
            "LEFT JOIN FETCH op.product p " +
            "WHERE o.user.id = :userId " +
            "ORDER BY o.id DESC")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    List<Order> findByUserIdWithProducts(@Param("userId") int userId);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderProducts op " +
            "LEFT JOIN FETCH op.product p " +
            "WHERE o.id IN :ids")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    List<Order> findAllWithProductsAndUser(@Param("ids") Collection<Integer> ids);

    @Query("SELECT o.id FROM Order o")
    Page<Integer> findAllOrderIdsWithProductsAndUser(Pageable pageable);
}
