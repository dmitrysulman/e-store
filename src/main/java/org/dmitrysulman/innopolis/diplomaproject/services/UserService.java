package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.User;

import java.util.Optional;

public interface UserService {
    public void save(User user);

    public Optional<User> findByEmail(String email);
}
