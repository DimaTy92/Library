package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        user.getBooks().forEach(book -> book.setUser(user));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Boolean takeBook(Long userId, Long bookId) {
        //todo Change to custom exception
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Such user does not exist"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Such book does not exist"));

        if (Objects.nonNull(book.getUser())) {
            throw new RuntimeException("The book is already reserved");
        }
        user.getBooks().add(book);
        book.setUser(user);
        return true;
    }

    @Override
    @Transactional
    public Boolean freeBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Such user does not exist"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Such book does not exist"));
        if (!user.getBooks().contains(book)) {
            throw new RuntimeException("User does not take this book");
        }
        user.getBooks().remove(book);
        book.setUser(null);
        return true;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("There is no such user's 'id' "));
    }

    @Override
    public User getUserByFirstName(String firstName) {
       return userRepository.findUserByFirstName(firstName);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public User updateUserById(Long userId, User user) {
        user.setId(userId);
        user.getBooks().forEach(book -> book.setUser(user));
        return userRepository.save(user);
    }

    @PostConstruct
    public void init() {
        User user = new User();
        user.setFirstName("Dima");
        user.setLastName("Tyshchenko");

        Book book1 = new Book();
        book1.setName("Game of Thrones");
        book1.setWriter("George Martin");
        book1.setUser(user);

        Book book2 = new Book();
        book2.setName("Pro Spring Boot");
        book2.setWriter("Felipe Gutierrez");
        book2.setUser(user);

        List<Book> listBooks = new ArrayList<>();
        listBooks.add(book1);
        listBooks.add(book2);


        user.setBooks(listBooks);

        userRepository.save(user);
    }

}
