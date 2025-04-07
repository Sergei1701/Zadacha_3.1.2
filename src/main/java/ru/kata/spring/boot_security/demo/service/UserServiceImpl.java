package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    @Transactional
    public void safeUser(User user) {
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List roleIds = user.getRoles().stream()
                    .map(Role::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Set resolvedRoles = new HashSet<>(roleService.findByIdRoles(roleIds));
            user.setRoles(resolvedRoles);
        }

        boolean isNew = user.getId() == 0;

        User targetUser = isNew ? new User() : userRepository.getUser(user.getId());

        targetUser.setName(user.getName());
        targetUser.setSurname(user.getSurname());
        targetUser.setCity(user.getCity());

        if (isNew || (user.getPassword() != null && !user.getPassword().isBlank())) {
            targetUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        targetUser.setRoles(user.getRoles());
        userRepository.safeUser(targetUser);
    }

    @Override
    public User getUser(long id) {
        return userRepository.getUser(id);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
