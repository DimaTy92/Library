package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User saveUser(User user);

    Boolean takeBook(Long userId, Long bookId);

    Boolean freeBook(Long userId, Long bookId);

    User getUserById(Long userId);

    void deleteUserById(Long userId);

    User updateUserById(Long userId, User user);
}


