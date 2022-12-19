package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.dto.UserDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.Order;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.repositories.CartRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.OrderRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.UserRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.PageableHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PageableHelper pageableHelper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CartRepository cartRepository,
                           OrderRepository orderRepository,
                           PageableHelper pageableHelper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.pageableHelper = pageableHelper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAdmin(false);
        user = userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User update(UserDto userDto, int userId) {
        User user = userRepository.findById(userId).orElse(null);
        user.setFirstName(userDto.getFirstName());
        user.setSecondName(userDto.getSecondName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setRepeatPassword(user.getPassword());

        return userRepository.save(user);
    }

    @Override
    public Page<User> findAll(Integer page, Integer perPage, String direction) {
        Pageable pageable = pageableHelper.preparePageable(page, perPage, direction, "id");
        Page<Integer> ids = userRepository.findAllUserIds(pageable);
        return getUsersEnrichedWithOrdersByIds(ids);
    }

    @Override
    public Page<User> findByNameOrEmailContaining(String query,
                                                  Integer page,
                                                  Integer perPage,
                                                  String direction) {
        Pageable pageable = pageableHelper.preparePageable(page, perPage, direction, "id");
        Page<Integer> ids = userRepository.findAllUserIdsByNameOrEmailContaining(query, pageable);
        return getUsersEnrichedWithOrdersByIds(ids);
    }

    private Page<User> getUsersEnrichedWithOrdersByIds(Page<Integer> ids) {
        List<User> users = userRepository.findAllWithOrders(ids.toList());
        List<Integer> orderIds = users.stream()
                .flatMap(user -> user.getOrders().stream())
                .map(Order::getId)
                .toList();
        orderRepository.findAllWithProductsAndUser(orderIds);
        return ids.map(id -> users.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .get()
        );
    }
}
