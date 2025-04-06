package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void safeUser(User user);

    User getUser(long id);

    void deleteUser(long id);

    User findByUsername(String username);
}
