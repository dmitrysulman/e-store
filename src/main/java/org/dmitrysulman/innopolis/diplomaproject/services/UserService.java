package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.UserDto;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);

    Optional<User> findByEmail(String email);

    User update(UserDto userDto, int userId);

    List<User> findAll();

    Page<User> findByNameOrEmailContaining(String query, Integer page, Integer perPage, String direction);
}
