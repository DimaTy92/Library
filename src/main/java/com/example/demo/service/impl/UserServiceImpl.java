package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.exception.AlreadyReservedException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    private static final String USER_NOT_FOUND = "User not found with id {%d}";
    private static final String BOOK_NOT_FOUND = "Book not found with id {%d}";

    @Override
    @Transactional(readOnly = true)
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND, userId)));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(String.format(BOOK_NOT_FOUND, bookId)));

        if (Objects.nonNull(book.getUser())) {
            throw new AlreadyReservedException("The book is already reserved");
        }
        user.getBooks().add(book);
        book.setUser(user);
        return true;
    }

    @Override
    @Transactional
    public Boolean freeBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND, userId)));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND, bookId)));
        if (!user.getBooks().contains(book)) {
            throw new NotFoundException(String.format("User with id: %d does not take book with id: %d ", userId, bookId));
        }
        user.getBooks().remove(book);
        book.setUser(null);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND, userId)));
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
