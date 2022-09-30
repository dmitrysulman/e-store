package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.UserDto;
import org.dmitrysulman.innopolis.diplomaproject.models.User;

import java.util.Optional;

public interface UserService {
    public User save(User user);

    public Optional<User> findByEmail(String email);

    public User update(UserDto userDto, int userId);

    public User findByIdWithOrders(int id);
}
