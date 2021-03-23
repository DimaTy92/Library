package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/{userId}/books/{bookId}")
    public Boolean takeBook(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        return userService.takeBook(userId, bookId);
    }

    @DeleteMapping("/{userId}/books/{bookId}")
    public Boolean freeBook(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        return userService.freeBook(userId, bookId);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/search")
    public User getUserByFirsName(@RequestParam("firstName") String firstName) {
        return userService.getUserByFirstName(firstName);
    }


    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }

    @PutMapping("/{userId}")
    public User updateUserById(@PathVariable("userId") Long userId, @RequestBody User user) {
        return userService.updateUserById(userId, user);
    }


}
