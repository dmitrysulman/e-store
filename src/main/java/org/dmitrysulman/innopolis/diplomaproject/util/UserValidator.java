package org.dmitrysulman.innopolis.diplomaproject.util;

import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> userWithSameEmail = userService.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && userWithSameEmail.get().getId() != user.getId()) {
            errors.rejectValue("email", "errors.user.email.unique");
        }
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            errors.rejectValue("repeatPassword", "errors.user.password.repeat");
        }
    }
}
