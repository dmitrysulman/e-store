package org.dmitrysulman.innopolis.diplomaproject.util;

import org.dmitrysulman.innopolis.diplomaproject.dto.UserDto;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserDtoValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserDtoValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto user = (UserDto) target;
        Optional<User> userWithSameEmail = userService.findByEmail(user.getEmail());
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userWithSameEmail.isPresent() && userWithSameEmail.get().getId() != userDetails.getUser().getId()) {
            errors.rejectValue("email", "errors.user.email.unique");
        }
    }
}
