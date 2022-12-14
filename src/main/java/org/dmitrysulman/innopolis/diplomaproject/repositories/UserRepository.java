package org.dmitrysulman.innopolis.diplomaproject.repositories;

import org.dmitrysulman.innopolis.diplomaproject.models.User;
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
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.orders o " +
            "WHERE u.id IN :ids")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    List<User> findAllWithOrders(@Param("ids") Collection<Integer> ids);

    @Query("SELECT u.id FROM User u " +
            "WHERE lower(u.firstName) LIKE lower(concat('%', :query, '%')) OR " +
            "lower(u.secondName) LIKE lower(concat('%', :query, '%')) OR " +
            "lower(u.email) LIKE lower(concat('%', :query, '%'))")
    Page<Integer> findAllUserIdsByNameOrEmailContaining(@Param("query") String query, Pageable pageable);

    @Query("SELECT u.id FROM User u")
    Page<Integer> findAllUserIds(Pageable pageable);
}
