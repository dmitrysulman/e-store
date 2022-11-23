package org.dmitrysulman.innopolis.diplomaproject.repositiries;

import org.dmitrysulman.innopolis.diplomaproject.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT DISTINCT(o) FROM Order o LEFT JOIN FETCH o.orderProducts op LEFT JOIN FETCH op.product p WHERE o.user.id = :userId ORDER BY o.id DESC")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    List<Order> findByUserIdWithProducts(@Param("userId") int userId);
}
