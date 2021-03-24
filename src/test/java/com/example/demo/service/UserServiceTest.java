package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.exception.AlreadyReservedException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllUsers_WhenDataExist_ThenReturnValidUsers() {
        User user = mock(User.class);
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> allUsers = userService.findAllUsers();

        assertThat(allUsers).hasSize(1)
                .containsOnly(user);
    }

    @Test
    void findAllUsers_WhenDataNotExist_ThenReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> allUsers = userService.findAllUsers();

        assertThat(allUsers).isEmpty();
    }

    @Test
    void saveUser_WhenContainsBook_ThenSetUserToBook() {
        User user = new User();
        Book book = new Book();
        user.getBooks().add(book);

        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.saveUser(user);
        assertThat(book.getUser()).isEqualTo(savedUser);
    }

    @Test
    void takeBook_WhenDataExists_ThenLinkUserAndBook() {
        Long userId = 1L;
        Long bookId = 2L;
        User user = new User();
        Book book = new Book();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        userService.takeBook(userId, bookId);

        assertThat(user.getBooks()).containsOnly(book);
        assertThat(book.getUser()).isEqualTo(user);
    }

    @Test
    void takeBook_WhenBookIsAlreadyReserved_ThenThrowException() {
        Long userId = 1L;
        Long bookId = 2L;
        User user = new User();
        Book book = new Book();
        book.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> userService.takeBook(userId, bookId))
                .isInstanceOf(AlreadyReservedException.class);
    }

    @Test
    void freeBook_WhenUserDoNotContainBook_ThenThrowException() {
        Long userId = 1L;
        Long bookId = 2L;
        User user = new User();
        Book book = new Book();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> userService.freeBook(userId, bookId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void freeBook_WhenUserContainBook_ThenFreeBook() {
        Long userId = 1L;
        Long bookId = 2L;
        User user = new User();
        Book book = new Book();
        user.getBooks().add(book);
        book.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        userService.freeBook(userId, bookId);

        assertThat(user.getBooks()).doesNotContain(book);
        assertThat(book.getUser()).isNull();
    }

    @Test
    void getUserById_WhenUserExists_ThenReturnUser() {
        User user = new User();
        when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));

        User userById = userService.getUserById(1L);

        assertThat(user).isEqualTo(userById);
    }

    @Test
    void getUserById_WhenUserDoesntExists_ThenThrowException() {
        when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(NotFoundException.class);
    }
}
