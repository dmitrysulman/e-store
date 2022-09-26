package org.dmitrysulman.innopolis.diplomaproject.repositiries;

import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
